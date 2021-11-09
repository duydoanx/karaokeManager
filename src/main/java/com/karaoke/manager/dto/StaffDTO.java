package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karaoke.manager.entity.support.Gender;
import lombok.Data;

@Data
public class StaffDTO {

  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  private String firstName;

  private String lastName;

  private Gender gender;

  private String address1;

  private String address2;

  private String phoneNumber;

  private String email;

  private String roleCodeName;

  private Integer status;
}
