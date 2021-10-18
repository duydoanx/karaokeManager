package com.karaoke.manager.service;

import com.karaoke.manager.entity.Permission;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;

import java.util.List;

public interface StaffUserService {

    Staff saveStaff(Staff staff);
    Role saveRole(Role role);
    Permission savePermission(Permission permission);
    void addPermissionToRole(List<String> permissionCodes, String roleCodeName);
    void addRoleToStaff(String username, String roleCodeName);
    Staff getStaff(String username);
    List<Staff> getStaffs();
}
