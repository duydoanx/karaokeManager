package com.karaoke.manager.dto;

import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.support.Gender;
import lombok.Data;

@Data
public class StaffDTO {

  private String username;

  private String firstName;

  private String lastName;

  private Gender gender;

  private String address1;

  private String address2;

  private String phoneNumber;

  private String email;

  private RoleDTO role;
}
