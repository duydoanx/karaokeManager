package com.karaoke.manager.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth")
public class FakeLogin {

  @GetMapping("/api/auth")
  public void login(@RequestParam String username, @RequestParam String password)
      throws IllegalAccessException {
    throw new IllegalAccessException("fake method");
  }
}
