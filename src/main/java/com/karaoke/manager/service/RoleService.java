package com.karaoke.manager.service;

import com.karaoke.manager.entity.Role;
import com.karaoke.manager.service.base.CrudEntityService;

public interface RoleService extends CrudEntityService<Role> {
  Role getRoleByCodeName(String codeName);
}
