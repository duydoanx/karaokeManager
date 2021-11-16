package com.karaoke.manager.api.support;

import com.karaoke.manager.dto.RoomBookingDTO;
import com.karaoke.manager.entity.BookingStatus;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.exception.RoomBookingException;
import com.karaoke.manager.mapper.RoomBookingMapper;
import com.karaoke.manager.service.OrderService;
import com.karaoke.manager.service.RoomService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomControllerSupport {
  public static RoomBooking checkRoomBookingIsBookedStatus(
      Long bookingId, RoomService roomService, OrderService orderService) {
    Optional<RoomBooking> roomBookingOptional = roomService.getRoomBookingById(bookingId);
    if (!roomBookingOptional.isPresent()) {
      throw new RuntimeException("Can not find room booking.");
    }

    RoomBooking roomBooking = roomBookingOptional.get();
    if (!roomBooking.getBookingStatus().getCodeName().equals(BookingStatus.BOOKED)) {
      throw new RuntimeException("Room booking must be booked status.");
    }

    return roomBooking;
  }

  public static RoomBooking createRoomBooking(
      RoomBookingDTO roomBookingDTO,
      Long time,
      String bookingStatusCode,
      RoomService roomService,
      RoomBookingMapper roomBookingMapper) {
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
        roomService.getBusyRoomBookingAroundTime(startTime, endTime, roomBookingDTO.getRoomId());
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
}
