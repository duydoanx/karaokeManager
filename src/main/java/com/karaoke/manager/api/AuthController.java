package com.karaoke.manager.api;

import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {

  private final StaffUserService staffUserService;

  @GetMapping("/api/auth")
  public void login(@RequestParam String username, @RequestParam String password)
      throws IllegalAccessException {
    throw new IllegalAccessException("fake method");
  }

  @Operation(
      summary = "Lấy access token",
      security = @SecurityRequirement(name = "JWT"),
      description =
          "Lưu ý: Bearer value (JWT) phải là refresh token. Logout nếu đã nhập access token cũ và nhập refresh token vào.",
      tags = {"Auth"})
  @GetMapping("/api/auth/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String rawToken = request.getHeader(AUTHORIZATION);
    if (rawToken != null && rawToken.startsWith("Bearer ")) {
      TokenUtils.VerifierObject verifier =
          TokenUtils.verifyToken(rawToken, SecurityConstant.REFRESH_TOKEN_SECRET_KEY);
      if (verifier.isValid()) {
        User user =
            TokenUtils.validVerifierObjectToUser(
                (TokenUtils.ValidVerifierObject) verifier, staffUserService::getStaff);
        Map<String, String> body = new HashMap<>();
        body.put("access_token", TokenUtils.accessTokenGenerate(user));
        HttpSupport.writeJsonObjectValue(response, body);
      } else {
        TokenUtils.invalidVerifierObjectResponse(
            (TokenUtils.InvalidVerifierObject) verifier, response);
      }
    }
  }
}
