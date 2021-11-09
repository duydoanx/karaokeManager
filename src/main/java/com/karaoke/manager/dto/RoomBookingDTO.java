package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomBookingDTO {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long bookingId;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Date startTime;

  private Long roomId;

  private String guestPhoneNumber;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String guestFullName;

  private String staffUserName;

  private String statusCodeName;
}
