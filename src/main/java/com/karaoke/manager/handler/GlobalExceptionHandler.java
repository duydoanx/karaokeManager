package com.karaoke.manager.handler;

import com.karaoke.manager.entity.support.ResponseApi;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  public ResponseApi<?> runtimeExceptionHandler(RuntimeException runtimeException) {
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), runtimeException.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public ResponseApi<?> runtimeExceptionHandler(HttpMessageNotReadableException runtimeException) {
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "Check parameters.");
  }

  @ExceptionHandler(value = {MissingRequestValueException.class})
  @ResponseBody
  public ResponseApi<?> handleConflict(MissingRequestValueException ex) {
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "Check requirement parameters");
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseBody
  public ResponseApi<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<FieldError> fieldError = ex.getFieldErrors();
    Map<String, String> errorData = new HashMap<>();
    fieldError.forEach(
        fieldError1 -> errorData.put(fieldError1.getField(), fieldError1.getDefaultMessage()));
    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "Invalid arguments.", errorData);
  }
}
