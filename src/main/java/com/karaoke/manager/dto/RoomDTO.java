package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.entity.RoomType;
import com.karaoke.manager.validation.annotation.StringContains;
import com.karaoke.manager.validation.group.room.CreateRoom;
import com.karaoke.manager.validation.group.room.UpdateRoom;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RoomDTO {

  public static final String RESERVED = "RESERVED";
  public static final String BOOKED = "BOOKED";
  public static final String EMPTY = "EMPTY";

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NotBlank(groups = {CreateRoom.class})
  private String name;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String roomBookedStatus;

  @StringContains(
      groups = {UpdateRoom.class},
      listString = {Room.ENABLE, Room.DISABLE},
      message = "Invalid statusCode.")
  private String statusCode;

  @StringContains(
      groups = {CreateRoom.class, UpdateRoom.class},
      listString = {RoomType.VIP, RoomType.NORMAL},
      message = "Invalid type.")
  @NotBlank(groups = {CreateRoom.class})
  private String type;
}
