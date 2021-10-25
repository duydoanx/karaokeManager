package com.karaoke.manager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpSupport {
  public static void writeJsonObjectValue(HttpServletResponse response, Map<String, String> body)
      throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), body);
  }

  public static void writeErrorMessage(
      HttpServletResponse response, String message, HttpStatus status) throws IOException {
    Map<String, String> body = new HashMap<>();
    body.put("error", message);
    response.setStatus(status.value());
    writeJsonObjectValue(response, body);
  }
}
