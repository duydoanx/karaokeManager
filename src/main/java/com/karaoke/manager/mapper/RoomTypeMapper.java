package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.RoomTypeDTO;
import com.karaoke.manager.entity.RoomType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoomTypeMapper {

  public abstract RoomTypeDTO roomTypeToRoomTypeDTO(RoomType roomType);
}
