package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.RoomDTO;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.service.RoomService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {RoomService.class})
public abstract class RoomMapper {

  @Autowired protected RoomService roomService;

  @Mapping(target = "type", expression = "java(room.getRoomType().getCodeName())")
  public abstract RoomDTO roomToRoomDTO(Room room);

  @Mapping(
      target = "roomType",
      expression = "java(roomService.getRoomTypeByCodeName(roomDTO.getType()))")
  public abstract Room roomDTOToRoom(RoomDTO roomDTO);
}
