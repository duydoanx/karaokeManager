package com.karaoke.manager.api;

import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.exception.BearerTokenException;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.token.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {

  private final StaffUserService staffUserService;

  @PostMapping(
      value = "/auth",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void login(@RequestPart String username, @RequestPart String password)
      throws IllegalAccessException {
    throw new IllegalAccessException("fake method");
  }

  @Operation(
      summary = "Lấy access token",
      security = @SecurityRequirement(name = "JWT"),
      description =
          "Lưu ý: Bearer value (JWT) phải là refresh token. Logout nếu đã nhập access token cũ và nhập refresh token vào.",
      tags = {"Auth"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    examples = {
                      @ExampleObject(
                          name = "Success",
                          value =
                              "{\n"
                                  + "    \"timestamp\": \"2021-11-03T21:11:06.157+00:00\",\n"
                                  + "    \"status\": 200,\n"
                                  + "    \"message\": \"success\",\n"
                                  + "    \"data\": {\n"
                                  + "        \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5hZ2VyIiwicm9sZSI6WyJST0xFX01BTkFHRVIiLCJyZWFkOnN0YWZmIl0sImV4cCI6MTYzNTk3NTY2Nn0.v6kht_6aFx-3UvToHPwKKK9D9a4KIM5Zz-nJvJQ9l0M\"\n"
                                  + "    }\n"
                                  + "}")
                    }))
      })
  @GetMapping("/auth/refresh")
  public ResponseApi<Map<String, String>> refreshToken(
      @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String rawToken) throws IOException {
    if (rawToken != null && rawToken.startsWith("Bearer ")) {
      TokenUtils.VerifierObject verifier =
          TokenUtils.verifyToken(rawToken, SecurityConstant.REFRESH_TOKEN_SECRET_KEY);
      if (verifier.isValid()) {
        User user =
            TokenUtils.validVerifierObjectToUser(
                (TokenUtils.ValidVerifierObject) verifier, staffUserService::getStaff);

        Map<String, String> body = new HashMap<>();
        body.put("access_token", TokenUtils.accessTokenGenerate(user));
        return new ResponseApi<Map<String, String>>(HttpStatus.OK.value(), body);
      } else {
        throw new BearerTokenException(
            ((TokenUtils.InvalidVerifierObject) verifier).getErrorMessage());
      }
    } else {
      throw new BearerTokenException("Bearer token did not find or did not start with 'Bearer'");
    }
  }
}
