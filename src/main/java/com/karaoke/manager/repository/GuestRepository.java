package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
  Optional<Guest> findByPhoneNumber(String phoneNumber);
}
