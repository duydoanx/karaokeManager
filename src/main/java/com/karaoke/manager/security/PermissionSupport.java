package com.karaoke.manager.security;

import org.springframework.stereotype.Service;

@Service("permissionSupport")
public class PermissionSupport {
  public String readStaff() {
    return StaffPermission.READ_STAFF.permissionCode;
  }

  public String writeStaff() {
    return StaffPermission.WRITE_STAFF.permissionCode;
  }
}
