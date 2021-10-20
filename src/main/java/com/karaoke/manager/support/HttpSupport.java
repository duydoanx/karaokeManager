package com.karaoke.manager.support;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpSupport {
  public static void writeJsonObjectValue(HttpServletResponse response, Map<String, String> body)
      throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), body);
  }
}
