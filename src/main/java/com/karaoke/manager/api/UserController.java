package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.repository.RoleRepository;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.DtoConvert;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
  private final StaffUserService staffUserService;
  private final RoleRepository roleRepository;
  private final DtoConvert dtoConvert;

  @GetMapping("/staffs")
  public ResponseEntity<List<StaffDTO>> getStaffs() {
    List<Staff> staffs = staffUserService.getStaffs();
    List<StaffDTO> staffDTOS =
        staffs.stream().map(dtoConvert::staffDTO).collect(Collectors.toList());
    return ResponseEntity.ok(staffDTOS);
  }

  @GetMapping("/auth/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String rawToken = request.getHeader(AUTHORIZATION);
    if (rawToken != null && rawToken.startsWith("Bearer ")) {
      TokenUtils.VerifierObject verifier =
              TokenUtils.verifyToken(rawToken, SecurityConstant.REFRESH_TOKEN_SECRET_KEY);

      if (verifier.isValid()) {
        User user = TokenUtils.validVerifierObjectToUser((TokenUtils.ValidVerifierObject) verifier, staffUserService::getStaff);
        Map<String, String> body = new HashMap<>();
        body.put("access_token", TokenUtils.accessTokenGenerate(user));
        HttpSupport.writeJsonObjectValue(response, body);
      } else {
        TokenUtils.invalidVerifierObjectResponse((TokenUtils.InvalidVerifierObject) verifier, response);
      }
    }
  }
}
