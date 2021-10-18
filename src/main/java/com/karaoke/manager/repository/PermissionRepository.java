package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Permission findByPermissionCode(String permissionCode);
}
