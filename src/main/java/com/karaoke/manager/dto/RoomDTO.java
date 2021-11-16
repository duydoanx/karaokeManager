package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String name;

  private String statusCode;

  private String type;
}
