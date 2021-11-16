package com.karaoke.manager.api;

import com.karaoke.manager.dto.RoomBookingDTO;
import com.karaoke.manager.entity.*;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.exception.RoomBookingException;
import com.karaoke.manager.mapper.ProductOrderedHistoryMapper;
import com.karaoke.manager.mapper.RoomBookingMapper;
import com.karaoke.manager.mapper.RoomMapper;
import com.karaoke.manager.service.OrderService;
import com.karaoke.manager.service.ProductService;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.karaoke.manager.api.support.RoomControllerSupport.createRoomBooking;

@RestController
@RequestMapping("/room-booking")
@RequiredArgsConstructor
public class RoomBookingController {

  private final RoomService roomService;
  private final OrderService orderService;
  private final ProductService productService;
  private final RoomMapper roomMapper;
  private final RoomBookingMapper roomBookingMapper;
  private final ProductOrderedHistoryMapper productOrderedHistoryMapper;
  private final TaskSchedulerService taskSchedulerService;

  // API đặt phòng trước
  @PostMapping("/reserve")
  public ResponseApi<RoomBookingDTO> reserveRoom(
      @RequestBody RoomBookingDTO roomBookingDTO,
      @Value("${application.revervation.time}") Long time) {

    Room enableRoom = roomService.getEnableRoom(roomBookingDTO.getRoomId());
    if (enableRoom == null) {
      throw new RuntimeException("The room has been disabled.");
    }
    // Create room booking with Pending status.
    RoomBooking roomBooking =
        createRoomBooking(
            roomBookingDTO, time, BookingStatus.PENDING, roomService, roomBookingMapper);

    /* Add task change booking status from pending to cancel if client don't check in become booked.
    Around 30 minutes. */
    taskSchedulerService.addCancelReservationRoomTask(
        roomBooking.getId(), new Date(roomBooking.getStartTime().getTime() + time));

    return new ResponseApi<>(
        HttpStatus.OK.value(), roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
  }

  // API đặt phòng trực tiếp
  @PostMapping("/booking")
  public ResponseApi<RoomBookingDTO> booking(
      @RequestBody RoomBookingDTO roomBookingDTO,
      @Value("${application.revervation.time}") Long time) {
    Room enableRoom = roomService.getEnableRoom(roomBookingDTO.getRoomId());
    if (enableRoom == null) {
      throw new RuntimeException("The room has been disabled.");
    }
    Optional<RoomBooking> currentBooked =
        roomService.getCurrentBookedByRoomId(roomBookingDTO.getRoomId());
    if (currentBooked.isPresent()) {
      throw new RoomBookingException(
          "The Room is on service.",
          roomBookingMapper.roomBookingToRoomBookingDTO(currentBooked.get()));
    }

    roomBookingDTO.setStartTime(new Date(System.currentTimeMillis()));

    // Create room booking with Booked status.
    RoomBooking roomBooking =
        createRoomBooking(
            roomBookingDTO, time, BookingStatus.BOOKED, roomService, roomBookingMapper);

    // Create an order for booked room.
    Order order = new Order();
    order.setRoomBooking(roomBooking);
    order.setStatus(orderService.getOrderStatusByCodeName(OrderStatus.PENDING));
    orderService.save(order);

    return new ResponseApi<>(
        HttpStatus.OK.value(), roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
  }

  // API huỷ đặt trước
  @GetMapping("/reserve/cancel/{bookingId}")
  public ResponseApi<?> cancelReservationRoom(@PathVariable Long bookingId) {
    Optional<RoomBooking> roomBookingOptional = roomService.getRoomBookingById(bookingId);
    if (!roomBookingOptional.isPresent()) {
      throw new RoomBookingException("Can not find room booking", null);
    }

    RoomBooking roomBooking = roomBookingOptional.get();
    if (roomBooking.getBookingStatus().getCodeName().equals(BookingStatus.PENDING)) {
      roomBooking.setBookingStatus(roomService.getBookingStatusByCodeName(BookingStatus.CANCEL));
      roomService.saveRoomBooking(roomBooking);
      return new ResponseApi<>(HttpStatus.OK.value());
    } else {
      throw new RoomBookingException(
          "The room is not in pending status",
          roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
    }
  }

  // API ckeck-in phòng đã đặt trước
  @GetMapping("/reserve/check-in/{bookingId}")
  public ResponseApi<?> checkIn(@PathVariable Long bookingId) {
    Optional<RoomBooking> roomBookingOptional = roomService.getRoomBookingById(bookingId);
    if (!roomBookingOptional.isPresent()) {
      throw new RoomBookingException("Can not find room booking", null);
    }

    RoomBooking roomBooking = roomBookingOptional.get();
    if (roomBooking.getRoom().getStatusCode().equals(Room.DISABLE)) {
      throw new RuntimeException("The room has been disabled.");
    }

    if (!roomBooking.getBookingStatus().getCodeName().equals(BookingStatus.PENDING)) {
      throw new RoomBookingException(
          "The room is not in pending status",
          roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
    }
    roomBooking.setBookingStatus(roomService.getBookingStatusByCodeName(BookingStatus.BOOKED));
    roomService.saveRoomBooking(roomBooking);

    // Create an order for booked room.
    Order order = new Order();
    order.setRoomBooking(roomBooking);
    order.setStatus(orderService.getOrderStatusByCodeName(OrderStatus.PENDING));
    orderService.save(order);

    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API xem danh sách đặt phòng của phòng hiện tại
  @GetMapping("/room/{roomId}")
  public ResponseApi<ResponsePage> getBookingRoomOfCurrentRoom(
      @RequestParam(required = false, defaultValue = "ALL") String status,
      @PathVariable Long roomId,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    List<String> var1 =
        Arrays.asList(
            BookingStatus.BOOKED, BookingStatus.PENDING, BookingStatus.DONE, BookingStatus.CANCEL);
    Page<RoomBooking> roomBookings;
    if (var1.contains(status.toUpperCase())) {
      roomBookings = roomService.getRoomBookingByRoomIdAndBookingStatus(roomId, status, pageable);
    } else if (status.equalsIgnoreCase("ALL")) {
      roomBookings = roomService.getRoomBookingByRoomId(roomId, pageable);
    } else {
      throw new RoomBookingException("Can not find status code", null);
    }
    ResponsePage responsePage =
        new ResponsePage(
            roomBookings.getContent().stream()
                .map(roomBookingMapper::roomBookingToRoomBookingDTO)
                .collect(Collectors.toList()),
            page,
            roomBookings.getTotalPages());
    return new ResponseApi<>(HttpStatus.OK.value(), responsePage);
  }

  // API xem room booking bang id
  @GetMapping("/booking/{bookingId}")
  public ResponseApi<RoomBookingDTO> getRoomBookingById(@PathVariable Long bookingId) {
    Optional<RoomBooking> roomBookingOptional = roomService.getRoomBookingById(bookingId);
    if (!roomBookingOptional.isPresent()) {
      throw new RuntimeException("Can not find room booking.");
    }

    return new ResponseApi<>(
        HttpStatus.OK.value(),
        roomBookingMapper.roomBookingToRoomBookingDTO(roomBookingOptional.get()));
  }
}
