package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.service.RoleService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {RoleService.class})
public abstract class StaffMapper {

  @Autowired protected RoleService roleService;

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(
      target = "role",
      expression = "java(roleService.getRoleByCodeName(staffDTO.getRoleCodeName()))")
  public abstract void updateStaffFromStaffDTO(StaffDTO staffDTO, @MappingTarget Staff staff);

  @Named("roleToRoleCodeName")
  public String roleToRoleCodeName(Role role) {
    return role.getCodeName();
  }

  @Mapping(source = "role", target = "roleCodeName", qualifiedByName = "roleToRoleCodeName")
  public abstract StaffDTO staffToStaffDTO(Staff staff);

  @Mapping(
      target = "role",
      expression = "java(roleService.getRoleByCodeName(staffDTO.getRoleCodeName()))")
  public abstract Staff staffDTOToStaff(StaffDTO staffDTO);
}
