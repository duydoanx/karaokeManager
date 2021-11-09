package com.karaoke.manager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomBookingException extends RuntimeException {

  private Object object;

  public RoomBookingException(String message, Object object) {
    super(message);
    this.object = object;
  }

  public RoomBookingException(String message, Throwable cause) {
    super(message, cause);
  }
}
