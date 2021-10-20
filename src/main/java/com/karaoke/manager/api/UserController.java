package com.karaoke.manager.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.repository.RoleRepository;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.support.DtoConvert;
import com.karaoke.manager.support.HttpSupport;
import com.karaoke.manager.support.TokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.writableHttpHeaders;

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
      try {
        String token = rawToken.substring("Bearer ".length());
        Algorithm algorithm =
            Algorithm.HMAC256(SecurityConstant.REFRESH_TOKEN_SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        Staff staff = staffUserService.getStaff(decodedJWT.getSubject());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(staff.getRole().getCodeName()));
        staff.getRole().getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode())));
        User user = new User(staff.getUsername(), staff.getPassword(), authorities);

        Map<String, String> body = new HashMap<>();
        body.put("access_token", TokenHelper.accessTokenGenerate(user));
        HttpSupport.writeJsonObjectValue(response, body);
      } catch (Exception e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader("error", e.getMessage());
        Map<String, String> body = new HashMap<>();
        body.put("error_message", e.getMessage());
        HttpSupport.writeJsonObjectValue(response, body);
      }
    }
  }
}
