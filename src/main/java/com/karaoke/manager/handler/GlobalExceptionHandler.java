package com.karaoke.manager.handler;

import com.karaoke.manager.entity.support.ResponseApi;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseApi<?> runtimeExceptionHandler(RuntimeException runtimeException) {
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), runtimeException.getMessage());
  }

  @ExceptionHandler(value = {MissingRequestValueException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseApi<?> handleConflict(MissingRequestValueException ex) {
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "Check requirement parameters");
  }
}
