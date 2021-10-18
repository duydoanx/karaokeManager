package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
