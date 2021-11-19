package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.RoomBookingDTO;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.service.GuestService;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.service.StaffUserService;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {RoomService.class, GuestService.class, StaffUserService.class})
public abstract class RoomBookingMapper {

  @Autowired protected RoomService roomService;
  @Autowired protected GuestService guestService;
  @Autowired protected StaffUserService staffUserService;

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(
      target = "orderId",
      expression =
          "java(roomBooking.getOrders().isEmpty() ? null : roomBooking.getOrders().get(0).getId())")
  @Mapping(target = "bookingId", source = "id")
  @Mapping(
      target = "statusCodeName",
      expression = "java(roomBooking.getBookingStatus().getCodeName())")
  @Mapping(target = "staffUserName", expression = "java(roomBooking.getStaff().getUsername())")
  @Mapping(
      target = "guestPhoneNumber",
      expression = "java(roomBooking.getGuest().getPhoneNumber())")
  @Mapping(target = "roomId", expression = "java(roomBooking.getRoom().getId())")
  @Mapping(
      target = "guestFullName",
      expression =
          "java(roomBooking.getGuest().getLastName() + \" \"+ roomBooking.getGuest().getFirstName())")
  public abstract RoomBookingDTO roomBookingToRoomBookingDTO(RoomBooking roomBooking);

  @Mapping(target = "orders", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(
      target = "staff",
      expression = "java(staffUserService.getStaff(roomBookingDTO.getStaffUserName()))")
  @Mapping(
      target = "room",
      expression = "java(roomService.findById(roomBookingDTO.getRoomId()).get())")
  @Mapping(
      target = "guest",
      expression =
          "java(guestService.getGuestPhoneNumberForMapper(roomBookingDTO.getGuestPhoneNumber()))")
  @Mapping(
      target = "bookingStatus",
      expression =
          "java(roomService.getBookingStatusByCodeName(roomBookingDTO.getStatusCodeName()))")
  public abstract RoomBooking roomBookingDTOToRoomBooking(RoomBookingDTO roomBookingDTO);
}
