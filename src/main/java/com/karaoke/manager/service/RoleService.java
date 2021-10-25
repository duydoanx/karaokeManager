package com.karaoke.manager.service;

import com.karaoke.manager.entity.Role;

public interface RoleService {
  Role getRoleByCodeName(String codeName);
}
