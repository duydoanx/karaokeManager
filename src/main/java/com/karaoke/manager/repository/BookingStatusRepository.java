package com.karaoke.manager.repository;

import com.karaoke.manager.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
  BookingStatus findByCodeName(String codeName);
}
