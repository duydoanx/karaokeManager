package com.karaoke.manager.api;

import com.karaoke.manager.api.support.OrderControllerSupport;
import com.karaoke.manager.dto.OrderDTO;
import com.karaoke.manager.entity.*;
import com.karaoke.manager.entity.support.ProductOrderKey;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.mapper.OrderMapper;
import com.karaoke.manager.service.OrderService;
import com.karaoke.manager.service.ProductService;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.utils.OrderPDFExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.karaoke.manager.api.support.RoomControllerSupport.checkRoomBookingIsBookedStatus;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final RoomService roomService;
  private final OrderService orderService;
  private final ProductService productService;
  private final OrderMapper orderMapper;
  private final OrderControllerSupport orderControllerSupport;

  // API thêm sản phẩm vào hoá đơn
  @PostMapping("/product")
  public ResponseApi<?> addProductToOrder(@RequestBody Map<String, Object> params) {
    Long bookingId;
    Long productId;
    Integer quantity;

    // kiem tra tinh hop le cua parameters
    if (params.containsKey("bookingId")) {
      bookingId = ((Number) params.get("bookingId")).longValue();
    } else {
      throw new RuntimeException("Missing bookingId.");
    }
    if (params.containsKey("productId")) {
      productId = ((Number) params.get("productId")).longValue();
    } else {
      throw new RuntimeException("Missing productId.");
    }
    if (params.containsKey("quantity")) {
      quantity = (Integer) params.get("quantity");
    } else {
      throw new RuntimeException("Missing quantity.");
    }

    // kiem tra room booking co ton tai va o trang thai booked chua
    checkRoomBookingIsBookedStatus(bookingId, roomService, orderService);
    Order order = orderService.getOrderByBookingId(bookingId);

    // xet xem co orderId va productId tuong ung chua, co thi tang so luong, chua co thi them moi
    Optional<ProductOrderedHistory> productOrderedHistoryOptional =
        orderService.getProductOrderedHistory(new ProductOrderKey(productId, order.getId()));
    if (productOrderedHistoryOptional.isPresent()) {
      ProductOrderedHistory productOrderedHistory = productOrderedHistoryOptional.get();
      productOrderedHistory.setQuantity(productOrderedHistory.getQuantity() + quantity);
      orderService.saveProductOrderedHistory(productOrderedHistory);
    } else {
      // kiem tra san pham co ton tai
      Optional<Product> productOptional = productService.findById(productId);
      if (productOptional.isPresent()) {
        ProductOrderedHistory productOrderedHistory = new ProductOrderedHistory();
        Product product = productOptional.get();
        productOrderedHistory.setId(new ProductOrderKey(productId, order.getId()));
        productOrderedHistory.setProduct(product);
        productOrderedHistory.setOrder(order);
        productOrderedHistory.setPrice(product.getPrice());
        productOrderedHistory.setQuantity(quantity);
        productOrderedHistory.setDescription(product.getDescription());
        orderService.saveProductOrderedHistory(productOrderedHistory);
      } else {
        throw new RuntimeException("Product does not exist.");
      }
    }
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  //   API xem hoá đơn theo booking id
  @GetMapping("/booking/{bookingId}")
  public ResponseApi<OrderDTO> getOrderByBookingId(@PathVariable Long bookingId) {

    // kiem tra room booking co o trang thai booked chua
    RoomBooking roomBooking = checkRoomBookingIsBookedStatus(bookingId, roomService, orderService);

    Order order = orderService.getOrderByBookingId(bookingId);

    return new ResponseApi<>(HttpStatus.OK.value(), orderMapper.orderToOrderDTO(order));
  }

  // API xem hoá đơn theo id
  @GetMapping("/{id}")
  public ResponseApi<OrderDTO> getOrder(@PathVariable Long id) {
    Optional<Order> orderOptional = orderService.findById(id);
    if (!orderOptional.isPresent()) {
      throw new RuntimeException("Can not find order.");
    }

    return new ResponseApi<>(
        HttpStatus.OK.value(), orderMapper.orderToOrderDTO(orderOptional.get()));
  }

  // API xem hoá đơn theo ngay
  @GetMapping("/day")
  public ResponseApi<ResponsePage> getOrdersByDay(
      @RequestBody Map<String, String> date,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());
    String regex = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
    if (!date.containsKey("date")) {
      throw new RuntimeException("Unable to find date.");
    }
    String dateStr = date.get("date");
    if (!dateStr.matches(regex)) {
      throw new RuntimeException("The date format must be YYYY-MM-DD.");
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1;
    try {
      date1 = dateFormat.parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException(e.getMessage());
    }
    Page<Order> ordersByDay = orderService.getOrdersByDay(date1, pageable);
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        new ResponsePage(
            ordersByDay.getContent().stream()
                .map(orderMapper::orderToOrderDTO)
                .collect(Collectors.toList()),
            page,
            ordersByDay.getTotalPages()));
  }

  // API xuất file PDF hoá đơn
  @GetMapping("/pdf/{orderId}")
  public void exportPDF(HttpServletResponse response, @PathVariable Long orderId)
      throws IOException {

    Optional<Order> orderOptional = orderService.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RuntimeException("Unable to find order.");
    }

    Order order = orderOptional.get();

    // kiểm tra trạng thái hoá đơn
    if (!order.getStatus().getCodeName().equals(OrderStatus.DONE)) {
      throw new RuntimeException("The order must be done.");
    }

    response.setContentType("application/pdf");

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=users_" + order.getId() + ".pdf";
    response.setHeader(headerKey, headerValue);

    OrderPDFExporter exporter = new OrderPDFExporter(order, "Karaoke");
    exporter.export(response);
  }

  // API thêm giảm giá hoá đơn theo mã hoá đơn
  @PostMapping("/discount/order-id/{orderId}")
  public ResponseApi<?> discountPercentByOrderId(
      @PathVariable Long orderId, @RequestBody Map<String, String> discountParam) {
    Optional<Order> orderOptional = orderService.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RuntimeException("Unable to find order.");
    }
    Order order = orderOptional.get();
    orderControllerSupport.setDiscount(discountParam, order);
    orderService.save(order);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API thêm giảm giá hoá đơn theo mã phiếu đặt phòng
  @PostMapping("/discount/booking-id/{bookingId}")
  public ResponseApi<?> discountPercentByBookingId(
      @PathVariable Long bookingId, @RequestBody Map<String, String> discountParam) {
    Order order = orderService.getOrderByBookingId(bookingId);
    if (order == null) {
      throw new RuntimeException("Unable to find order.");
    }
    orderControllerSupport.setDiscount(discountParam, order);
    orderService.save(order);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API thanh toán hoá đơn
  @GetMapping("/pay/{orderId}")
  public ResponseApi<OrderDTO> pay(@PathVariable Long orderId) {
    Optional<Order> orderOptional = orderService.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RuntimeException("Unable to find order.");
    }

    // Tính số giờ
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Order order = orderOptional.get();
    long time = timestamp.getTime() - order.getRoomBooking().getStartTime().getTime();
    double hourNumber = ((double) time) / 1000 / 60 / 60;

    // Làm tròn 1 chữ số sau dấu phẩy
    hourNumber = Math.round(hourNumber * 10.0) / 10.0;
    order.setNumberHoursBooked(hourNumber);

    // Tính tổng tiền
    double hourMoney = hourNumber * order.getRoomBooking().getRoom().getRoomType().getPrice();
    double productMoney =
        order.getProductOrderedHistories().stream()
            .mapToDouble(
                productOrderedHistory ->
                    productOrderedHistory.getQuantity() * productOrderedHistory.getPrice())
            .sum();
    double totalMoney = (hourMoney + productMoney);
    totalMoney -= order.getDiscountMoney();
    totalMoney -= (totalMoney * order.getDiscountPercent()) / 100;
    totalMoney = Math.round(totalMoney);
    order.setTotal(totalMoney);
    order.setStatus(orderService.getOrderStatusByCodeName(OrderStatus.DONE));

    order
        .getRoomBooking()
        .setBookingStatus(roomService.getBookingStatusByCodeName(BookingStatus.DONE));

    order = orderService.save(order);
    return new ResponseApi<>(HttpStatus.OK.value(), orderMapper.orderToOrderDTO(order));
  }

  // API huy hoa don
  @GetMapping("/cancel/{orderId}")
  public ResponseApi<?> cancelOrder(@PathVariable Long orderId) {
    Optional<Order> orderOptional = orderService.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RuntimeException("Unable to find order.");
    }
    Order order = orderOptional.get();
    if (!order.getStatus().getCodeName().equals(OrderStatus.PENDING)) {
      throw new RuntimeException("Order status must be PENDING.");
    }
    order.setStatus(orderService.getOrderStatusByCodeName(OrderStatus.CANCEL));
    order
        .getRoomBooking()
        .setBookingStatus(roomService.getBookingStatusByCodeName(BookingStatus.CANCEL));
    orderService.save(order);
    return new ResponseApi<>(HttpStatus.OK.value());
  }
}
