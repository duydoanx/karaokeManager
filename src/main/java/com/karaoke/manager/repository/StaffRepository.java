package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {

  @EntityGraph(value = "staff-with-roles")
  Staff findByUsername(String username);

  Staff findByPhoneNumber(String phoneNumber);

  Staff findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByEmail(String email);
}
