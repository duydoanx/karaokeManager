package com.karaoke.manager.service;

import com.karaoke.manager.entity.BookingStatus;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.entity.RoomType;
import com.karaoke.manager.service.base.CrudEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RoomService extends CrudEntityService<Room> {

  Room getEnableRoom(Long id);

  Page<Room> getEnableRooms(Pageable pageable);

  Room getDisableRoom(Long id);

  RoomType getRoomTypeByCodeName(String codeName);

  BookingStatus getBookingStatusByCodeName(String codeName);

  Page<Room> getRooms(Pageable pageable);

  Page<Room> getEmptyRoomsAroundTime(Timestamp startTime, Timestamp endTime, Pageable pageable);

  Page<Room> getBookedRoom(Pageable pageable);

  RoomBooking addRoomBooking(RoomBooking roomBooking);

  void updateRoomBooking(RoomBooking roomBooking);

  Optional<RoomBooking> getRoomBookingById(Long id);

  List<RoomBooking> getReservationRooms();

  List<RoomBooking> getRoomBookingByBetweenTimeAndRoomId(
      Timestamp startTime, Timestamp endTime, Long roomId);

  List<RoomBooking> getBusyRoomBookingAroundTime(
      Timestamp startTime, Timestamp endTime, Long roomId);

  Boolean isExistRoomBookingByBetweenTimeAndRoomId(
      Timestamp startTime, Timestamp endTime, Long roomId);

  RoomBooking saveRoomBooking(RoomBooking roomBooking);

  Boolean roomIsAvailable(Long roomId);

  Optional<RoomBooking> getCurrentBookedByRoomId(Long roomId);

  Page<RoomBooking> getRoomBookingByRoomId(Long roomId, Pageable pageable);

  Page<RoomBooking> getRoomBookingByRoomIdAndBookingStatus(
      Long roomId, String bookingStatus, Pageable pageable);
}
