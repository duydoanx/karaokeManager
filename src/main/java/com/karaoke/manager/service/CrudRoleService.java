package com.karaoke.manager.service;

import com.karaoke.manager.entity.Role;
import com.karaoke.manager.repository.RoleRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.stereotype.Service;

@Service
public class CrudRoleService extends CrudBaseEntityService<Role> implements RoleService {

  private final RoleRepository roleRepository;

  public CrudRoleService(RoleRepository roleRepository) {
    super(roleRepository);
    this.roleRepository = roleRepository;
  }

  @Override
  public Role getRoleByCodeName(String codeName) {
    return roleRepository.findByCodeName(codeName);
  }
}
