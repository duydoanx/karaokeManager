package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Order findByRoomBooking_Id(Long id);

  Page<Order> findByCreatedAtBetween(
      Timestamp createdAtStart, Timestamp createdAtEnd, Pageable pageable);

  List<Order> findByCreatedAtBetweenAndStatus_CodeName(
      Timestamp createdAtStart, Timestamp createdAtEnd, String codeName);
}
