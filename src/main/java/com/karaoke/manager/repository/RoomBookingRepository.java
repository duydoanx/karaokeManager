package com.karaoke.manager.repository;

import com.karaoke.manager.entity.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
}
