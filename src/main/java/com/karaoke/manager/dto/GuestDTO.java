package com.karaoke.manager.dto;

import com.karaoke.manager.entity.support.Gender;
import com.karaoke.manager.validation.annotation.EnumContains;
import com.karaoke.manager.validation.group.guest.CreateGuest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GuestDTO {

  private Long id;

  @NotBlank(groups = {CreateGuest.class})
  private String firstName;

  @NotBlank(groups = {CreateGuest.class})
  private String lastName;

  @EnumContains(
      enumClass = Gender.class,
      message = "Invalid gender.",
      groups = {CreateGuest.class})
  private String gender;

  @NotBlank(groups = {CreateGuest.class})
  private String address1;

  private String address2;

  @NotBlank(groups = {CreateGuest.class})
  private String phoneNumber;

  @NotBlank(groups = {CreateGuest.class})
  @Email(groups = {CreateGuest.class})
  private String email;

  private Integer status;
}
