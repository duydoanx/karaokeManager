package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.GuestDTO;
import com.karaoke.manager.entity.Guest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class GuestMapper {

  @Mapping(target = "roomBookings", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  public abstract Guest guestDTOToGuest(GuestDTO guestDTO);

  public abstract GuestDTO guestToGuestDTO(Guest guest);

  @Mapping(target = "roomBookings", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract void updateGuestFromGuestDTO(GuestDTO guestDTO, @MappingTarget Guest guest);
}
