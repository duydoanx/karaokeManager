package com.karaoke.manager.repository;

import com.karaoke.manager.entity.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
  List<RoomBooking> findByStartTimeIsBetweenAndRoom_Id(
      Timestamp startTimeStart, Timestamp startTimeEnd, Long id);

  boolean existsByStartTimeBetweenAndRoom_Id(
      Timestamp startTimeStart, Timestamp startTimeEnd, Long id);

  List<RoomBooking> findByBookingStatus_CodeName(String codeName);

  boolean existsByBookingStatus_CodeNameAndRoom_Id(String codeName, Long id);

  List<RoomBooking> findByRoom_Id(Long id);

  List<RoomBooking> findByBookingStatus_CodeNameAndRoom_Id(String codeName, Long id);
}
