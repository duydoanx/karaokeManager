package com.karaoke.manager.dto;

import com.karaoke.manager.entity.support.Gender;
import com.karaoke.manager.validation.annotation.EnumContains;
import com.karaoke.manager.validation.group.staff.Create;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GuestDTO {

  private Long id;

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

  private Integer status;
}
