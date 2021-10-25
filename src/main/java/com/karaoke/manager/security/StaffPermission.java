package com.karaoke.manager.security;

public enum StaffPermission {
  READ_STAFF("read:staff"),
  WRITE_STAFF("write:staff");

  public final String permissionCode;

  StaffPermission(String permissionCodeName) {
    this.permissionCode = permissionCodeName;
  }
}
