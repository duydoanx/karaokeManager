package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface RoomRepository extends JpaRepository<Room, Long> {
  Room findByIdAndStatusCode(Long id, String statusCode);

  Page<Room> findByStatusCode(String statusCode, Pageable pageable);

  @Query(
      "select r from Room r left join r.roomBookings roomBookings where roomBookings.bookingStatus.codeName = :codeName group by r.id")
  Page<Room> findByRoomBookings_BookingStatus_CodeName(
      @Param("codeName") String codeName, Pageable pageable);

  @Query(
      "select r from Room  r where not exists (select r1 from Room r1 left join r1.roomBookings roomBookings where (roomBookings.bookingStatus.codeName like %?1% or (roomBookings.bookingStatus.codeName like %?2% and roomBookings.startTime between ?3 and ?4)) and r.id = r1.id)")
  Page<Room> findEmptyRooms(
      String bookedCodeName,
      String pendingCodeName,
      Timestamp startTimeStart,
      Timestamp startTimeEnd,
      Pageable pageable);
}
