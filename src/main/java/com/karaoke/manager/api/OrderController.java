package com.karaoke.manager.api;

import com.karaoke.manager.dto.OrderDTO;
import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.Product;
import com.karaoke.manager.entity.ProductOrderedHistory;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.entity.support.ProductOrderKey;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.mapper.OrderMapper;
import com.karaoke.manager.service.OrderService;
import com.karaoke.manager.service.ProductService;
import com.karaoke.manager.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.karaoke.manager.api.support.RoomControllerSupport.checkRoomBookingIsBookedStatus;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final RoomService roomService;
  private final OrderService orderService;
  private final ProductService productService;
  private final OrderMapper orderMapper;

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
}
