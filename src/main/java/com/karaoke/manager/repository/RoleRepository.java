package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByCodeName(String codeName);
}
