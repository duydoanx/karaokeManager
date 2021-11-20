package com.karaoke.manager.handler;

import com.karaoke.manager.entity.support.ResponseApi;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(value = 0)
public class ParameterExceptionHandler {
  @ExceptionHandler(value = {MissingRequestHeaderException.class})
  @ResponseBody
  public ResponseApi<?> handleConflict(MissingRequestHeaderException ex) {
    return new ResponseApi<>(
        HttpStatus.BAD_REQUEST.value(), "Missing parameter header: " + ex.getHeaderName());
  }
}
