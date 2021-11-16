package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.support.Gender;
import com.karaoke.manager.validation.annotation.EnumContains;
import com.karaoke.manager.validation.annotation.StringContains;
import com.karaoke.manager.validation.group.staff.Create;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StaffDTO {

  @NotBlank(groups = {Create.class})
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(groups = {Create.class})
  private String password;

  @NotBlank(groups = {Create.class})
  private String firstName;

  @NotBlank(groups = {Create.class})
  private String lastName;

  @EnumContains(
      enumClass = Gender.class,
      message = "Invalid gender.",
      groups = {Create.class})
  private String gender;

  @NotBlank(groups = {Create.class})
  private String address1;

  private String address2;

  @NotBlank(groups = {Create.class})
  private String phoneNumber;

  @NotBlank(groups = {Create.class})
  @Email(groups = {Create.class})
  private String email;

  @NotBlank(groups = {Create.class})
  @NotNull(groups = {Create.class})
  @StringContains(
      listString = {Role.STAFF, Role.MANAGER, Role.ACCOUNTANT},
      message = "Invalid role code name.",
      groups = {Create.class})
  private String roleCodeName;

  private Integer status;
}
