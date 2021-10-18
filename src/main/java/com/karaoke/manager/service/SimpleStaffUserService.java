package com.karaoke.manager.service;

import com.karaoke.manager.entity.Permission;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.repository.PermissionRepository;
import com.karaoke.manager.repository.RoleRepository;
import com.karaoke.manager.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service @Transactional @Slf4j
public class SimpleStaffUserService implements StaffUserService, UserDetailsService {

  private final StaffRepository staffRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    Staff staff = staffRepository.findByUsername(s);
    List<Permission> permissions = staff.getRole().getPermissions();
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(staff.getRole().getCodeName()));
    permissions.forEach(
        permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode())));
    return new User(staff.getUsername(), staff.getPassword(), authorities);
  }

  @Override
  public Staff saveStaff(Staff staff) {
    log.info("Saving staff {}", staff.getUsername());
    return staffRepository.save(staff);
  }

  @Override
  public Role saveRole(Role role) {
    log.info("Saving role {}", role.getCodeName());
    return roleRepository.save(role);
  }

  @Override
  public Permission savePermission(Permission permission) {
    log.info("Saving permission {}", permission.getPermissionCode());
    return permissionRepository.save(permission);
  }

  @Override
  public void addPermissionToRole(List<String> permissionCodes, String roleCodeName) {
    log.info("Adding permission {} to role {}", permissionCodes, roleCodeName);
    Role role = roleRepository.findByCodeName(roleCodeName);
    List<Permission> permissions = new ArrayList<>();
    permissionCodes.forEach(permissionCode -> permissions.add(permissionRepository.findByPermissionCode(permissionCode)));
    role.getPermissions().addAll(permissions);
  }

  @Override
  public void addRoleToStaff(String username, String roleCodeName) {
    log.info("Adding staff {} to role {}", username, roleCodeName);
    Staff staff = staffRepository.findByUsername(username);
    Role role = roleRepository.findByCodeName(roleCodeName);
    staff.setRole(role);
  }

  @Override
  public Staff getStaff(String username) {
    log.info("Fetching staff {}", username);
    return staffRepository.findByUsername(username);
  }

  @Override
  public List<Staff> getStaffs() {
    log.info("Fetching staffs");
    return staffRepository.findAll();
  }
}
