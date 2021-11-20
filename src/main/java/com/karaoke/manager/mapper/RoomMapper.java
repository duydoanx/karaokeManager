package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.RoomDTO;
import com.karaoke.manager.entity.BookingStatus;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.service.RoomService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {RoomService.class})
public abstract class RoomMapper {

  @Autowired protected RoomService roomService;

  @Mapping(target = "roomBookedStatus", ignore = true)
  @Mapping(target = "type", expression = "java(room.getRoomType().getCodeName())")
  public abstract RoomDTO roomToRoomDTO(Room room);

  @Mapping(target = "roomBookings", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(
      target = "roomType",
      expression = "java(roomService.getRoomTypeByCodeName(roomDTO.getType()))")
  public abstract Room roomDTOToRoom(RoomDTO roomDTO);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(
      target = "roomType",
      expression =
          "java(roomDTO.getType() != null ? roomService.getRoomTypeByCodeName(roomDTO.getType()) : room.getRoomType())")
  @Mapping(target = "roomBookings", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  public abstract void updateRoomFromRoomDTO(RoomDTO roomDTO, @MappingTarget Room room);

  @AfterMapping
  protected void setRoomStatus(@MappingTarget RoomDTO roomDTO) {
    if (roomService.existRoomBookingByStatusCodeAndRoomId(BookingStatus.BOOKED, roomDTO.getId())) {
      roomDTO.setRoomBookedStatus(RoomDTO.BOOKED);
    } else if (roomService.existRoomBookingByStatusCodeAndRoomId(
        BookingStatus.PENDING, roomDTO.getId())) {
      roomDTO.setRoomBookedStatus(RoomDTO.RESERVED);
    } else {
      roomDTO.setRoomBookedStatus(RoomDTO.EMPTY);
    }
  }
}
