package com.karaoke.manager;

import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.service.TaskSchedulerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

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
  @Order(value = 1)
  CommandLineRunner autoCancelReservationRoom(
      RoomService roomService,
      TaskSchedulerService taskSchedulerService,
      @Value("${application.revervation.time}") Long time) {
    return args -> {
      List<RoomBooking> reservationRooms = roomService.getReservationRooms();
      reservationRooms.forEach(
          roomBooking -> {
            taskSchedulerService.addCancelReservationRoomTask(
                roomBooking.getId(), new Date(roomBooking.getStartTime().getTime() + time));
          });
    };
  }

  //  @Bean
  //  CommandLineRunner runner(StaffUserService userService) {
  //    return args -> {
  //      userService.savePermission(new Permission("read:staff", "Read staff"));
  //      userService.savePermission(new Permission("write:staff", "Write staff"));
  //
  //      userService.saveRole(new Role("Administrator", "ADMIN"));
  //      userService.saveRole(new Role("Manager", "MANAGER"));
  //
  //      userService.saveStaff(
  //          new Staff(
  //              "admin",
  //              "123456",
  //              "Admin",
  //              "Admin",
  //              Gender.MALE,
  //              "HCM",
  //              null,
  //              "0123456789",
  //              "admin@admin",
  //              1));
  //
  //      userService.saveStaff(
  //          new Staff(
  //              "manager",
  //              "1234567",
  //              "manager",
  //              "manager",
  //              Gender.MALE,
  //              "HCM1",
  //              null,
  //              "0123456789",
  //              "manager@manager",
  //              1));
  //
  //      userService.addPermissionToRole(Arrays.asList("read:staff", "write:staff"), "ADMIN");
  //      userService.addPermissionToRole(Collections.singletonList("read:staff"), "Manager");
  //
  //      userService.addRoleToStaff("admin", "ADMIN");
  //      userService.addRoleToStaff("manager", "MANAGER");
  //    };
  //  }
}
