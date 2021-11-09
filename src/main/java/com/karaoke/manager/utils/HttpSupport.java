package com.karaoke.manager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karaoke.manager.entity.support.ResponseApi;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpSupport {

  public static <T> void writeJsonMapObjectValue(HttpServletResponse response, T object)
      throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), object);
  }

  public static void writeErrorMessage(
      HttpServletResponse response, String message, HttpStatus status) throws IOException {
    response.setStatus(status.value());
    writeJsonMapObjectValue(response, new ResponseApi<>(status.value(), message));
  }
}
