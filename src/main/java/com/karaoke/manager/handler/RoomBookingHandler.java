package com.karaoke.manager.handler;

import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.exception.RoomBookingException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(value = 1)
public class RoomBookingHandler {

  @ExceptionHandler(RoomBookingException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseApi<?> roomBookingExceptionHandler(RoomBookingException roomBookingException) {
    return new ResponseApi<>(
        HttpStatus.BAD_REQUEST.value(),
        roomBookingException.getMessage(),
        roomBookingException.getObject());
  }
}
