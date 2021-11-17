package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.support.Gender;
import com.karaoke.manager.validation.annotation.EnumContains;
import com.karaoke.manager.validation.annotation.StringContains;
import com.karaoke.manager.validation.group.staff.CreateStaff;
import com.karaoke.manager.validation.group.staff.UpdateStaff;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StaffDTO {

  @NotBlank(groups = {CreateStaff.class})
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(groups = {CreateStaff.class})
  private String password;

  @NotBlank(groups = {CreateStaff.class})
  private String firstName;

  @NotBlank(groups = {CreateStaff.class})
  private String lastName;

  @EnumContains(
      enumClass = Gender.class,
      message = "Invalid gender.",
      groups = {CreateStaff.class, UpdateStaff.class})
  private String gender;

  @NotBlank(groups = {CreateStaff.class})
  private String address1;

  private String address2;

  @NotBlank(groups = {CreateStaff.class})
  private String phoneNumber;

  @NotBlank(groups = {CreateStaff.class})
  @Email(groups = {CreateStaff.class})
  private String email;

  @NotBlank(groups = {CreateStaff.class})
  @NotNull(groups = {CreateStaff.class})
  @StringContains(
      listString = {Role.STAFF, Role.MANAGER, Role.ACCOUNTANT},
      message = "Invalid role code name.",
      groups = {CreateStaff.class, UpdateStaff.class})
  private String roleCodeName;

  private Integer status;
}
