package com.karaoke.manager;

import com.karaoke.manager.entity.Permission;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.entity.support.Gender;
import com.karaoke.manager.service.StaffUserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class ManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ManagerApplication.class, args);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  CommandLineRunner runner(StaffUserService userService) {
    return args -> {
      userService.savePermission(new Permission("read:staff", "Read staff"));
      userService.savePermission(new Permission("write:staff", "Write staff"));

      userService.saveRole(new Role("Administrator", "ADMIN"));
      userService.saveRole(new Role("Manager", "MANAGER"));

      userService.saveStaff(
          new Staff(
              "admin",
              "123456",
              "Admin",
              "Admin",
              Gender.MALE,
              "HCM",
              null,
              "0123456789",
              "admin@admin"));

      userService.saveStaff(
              new Staff(
                      "manager",
                      "1234567",
                      "manager",
                      "manager",
                      Gender.MALE,
                      "HCM1",
                      null,
                      "0123456789",
                      "manager@manager"));

      userService.addPermissionToRole(Arrays.asList("read:user", "write:user"), "ADMIN");
      userService.addPermissionToRole(Collections.singletonList("read:user"), "Manager");

      userService.addRoleToStaff("admin", "ADMIN");
      userService.addRoleToStaff("manager", "MANAGER");
    };
  }
}
