package com.karaoke.manager.api;

import com.karaoke.manager.dto.RoomBookingDTO;
import com.karaoke.manager.dto.RoomDTO;
import com.karaoke.manager.entity.*;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.exception.RoomBookingException;
import com.karaoke.manager.mapper.RoomBookingMapper;
import com.karaoke.manager.mapper.RoomMapper;
import com.karaoke.manager.service.OrderService;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final OrderService orderService;
  private final RoomMapper roomMapper;
  private final RoomBookingMapper roomBookingMapper;
  private final TaskSchedulerService taskSchedulerService;

  @GetMapping("/rooms")
  @ResponseStatus(HttpStatus.OK)
  public ResponseApi<List<RoomDTO>> getAllRoom() {
    List<Room> rooms = roomService.getRooms();
    List<RoomDTO> roomDTOS =
        rooms.stream().map(roomMapper::roomToRoomDTO).collect(Collectors.toList());
    return new ResponseApi<>(HttpStatus.OK.value(), roomDTOS);
  }

  @PostMapping("/rooms/reserve")
  public ResponseApi<RoomBookingDTO> reserveRoom(
      @RequestBody RoomBookingDTO roomBookingDTO,
      @Value("${application.revervation.time}") Long time) {
    // Create room booking with Pending status.
    RoomBooking roomBooking = createRoomBooking(roomBookingDTO, time, BookingStatus.PENDING);

    /* Add task change booking status from pending to cancel if client don't check in become booked.
    Around 30 minutes. */
    taskSchedulerService.addCancelReservationRoomTask(
        roomBooking.getId(), new Date(roomBooking.getStartTime().getTime() + time));

    return new ResponseApi<>(
        HttpStatus.OK.value(), roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
  }

  @PostMapping("/rooms/booking")
  public ResponseApi<RoomBookingDTO> booking(
      @RequestBody RoomBookingDTO roomBookingDTO,
      @Value("${application.revervation.time}") Long time) {
    Optional<RoomBooking> currentBooked =
        roomService.getCurrentBookedByRoomId(roomBookingDTO.getRoomId());
    if (currentBooked.isPresent()) {
      throw new RoomBookingException(
          "The Room is on service.",
          roomBookingMapper.roomBookingToRoomBookingDTO(currentBooked.get()));
    }

    roomBookingDTO.setStartTime(new Date(System.currentTimeMillis()));

    // Create room booking with Booked status.
    RoomBooking roomBooking = createRoomBooking(roomBookingDTO, time, BookingStatus.BOOKED);

    // Create an order for booked room.
    Order order = new Order();
    order.setRoomBooking(roomBooking);
    order.setStatus(orderService.getOrderStatusByCodeName(OrderStatus.PENDING));
    orderService.save(order);

    return new ResponseApi<>(
        HttpStatus.OK.value(), roomBookingMapper.roomBookingToRoomBookingDTO(roomBooking));
  }

  private RoomBooking createRoomBooking(
      RoomBookingDTO roomBookingDTO, Long time, String bookingStatusCode) {
    Optional<Room> room = roomService.findById(roomBookingDTO.getRoomId());
    if (!room.isPresent()) {
      throw new RoomBookingException("Can not find that room", null);
    } else if (room.get().getStatusCode().equals(Room.DISABLE)) {
      throw new RoomBookingException("The room has been disabled", null);
    }

    // Check start time must be after now and max 2 weeks later.
    if (!bookingStatusCode.equals(BookingStatus.BOOKED)
        && !roomBookingDTO.getStartTime().after(new Timestamp(System.currentTimeMillis()))) {
      throw new RoomBookingException("The booking time must be after the current time.", null);
    }
    if (!roomBookingDTO
        .getStartTime()
        .before(Timestamp.valueOf(LocalDateTime.now().plusWeeks(2L)))) {
      throw new RoomBookingException("The booking time should not exceed 2 weeks.", null);
    }

    // Set current staff
    roomBookingDTO.setStaffUserName(
        ((Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getUsername());

    // Check room booking start time to others.

    Timestamp startTime = new Timestamp(roomBookingDTO.getStartTime().getTime() - time);
    Timestamp endTime = new Timestamp(roomBookingDTO.getStartTime().getTime() + time);
    List<RoomBooking> roomBookings =
        roomService.getRoomBookingAroundTime(startTime, endTime, roomBookingDTO.getRoomId());
    if (!roomBookings.isEmpty()) {
      throw new RoomBookingException(
          "The room has been reserved or is on service. See detail in data.",
          roomBookings.stream()
              .map(roomBookingMapper::roomBookingToRoomBookingDTO)
              .collect(Collectors.toList()));
    }

    RoomBooking roomBooking = roomBookingMapper.roomBookingDTOToRoomBooking(roomBookingDTO);
    roomBooking.setBookingStatus(roomService.getBookingStatusByCodeName(bookingStatusCode));
    roomBooking = roomService.addRoomBooking(roomBooking);
    return roomBooking;
  }

  // API huỷ đặt trước
  @GetMapping("/rooms/reserve/cancel/{bookingId}")
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
  @GetMapping("/rooms/reserve/check-in/{bookingId}")
  public ResponseApi<?> checkIn(@PathVariable Long bookingId) {
    Optional<RoomBooking> roomBookingOptional = roomService.getRoomBookingById(bookingId);
    if (!roomBookingOptional.isPresent()) {
      throw new RoomBookingException("Can not find room booking", null);
    }

    RoomBooking roomBooking = roomBookingOptional.get();
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

  // API thêm sản phẩm vào hoá đơn

  // API xem hoá đơn theo booking id

  // API xem danh sách phòng trống trong khoảng thời gian từ a -> b

  // API xem danh sách phòng đang được dùng.

  // API Kiểm tra xem phòng có đang được sử dụng hay không

  // API thêm phòng mới

  // API sửa thông tin phòng

  // API vô hiệu hoá phòng
}
