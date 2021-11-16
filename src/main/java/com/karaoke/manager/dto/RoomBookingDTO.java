package com.karaoke.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomBookingDTO {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long bookingId;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Date startTime;

  private Long roomId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long orderId;

  private String guestPhoneNumber;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String guestFullName;

  private String staffUserName;

  private String statusCodeName;
}
