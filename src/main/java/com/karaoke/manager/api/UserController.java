package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.mapper.StaffMapper;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Transactional
@Tag(name = "Staff")
public class UserController {
  private final StaffUserService staffUserService;
  private final StaffMapper staffMapper;

  @Operation(summary = "Lấy danh sách nhân viên", security = @SecurityRequirement(name = "JWT"))
  @PreAuthorize("hasAuthority(@permissionSupport.readStaff())")
  @GetMapping("/staffs")
  public List<StaffDTO> getStaffs() {
    List<Staff> staffs = staffUserService.getStaffs();
    return staffs.stream().map(staffMapper::staffToStaffDTO).collect(Collectors.toList());
  }

  @Operation(summary = "Tạo nhân viên mới", security = @SecurityRequirement(name = "JWT"))
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/staffs")
  public StaffDTO createStaff(@RequestBody StaffDTO staffDTO) {
    return staffMapper.staffToStaffDTO(
        staffUserService.saveStaff(staffMapper.staffDTOToStaff(staffDTO)));
  }

  @Operation(
      summary = "Lấy access token",
      security = @SecurityRequirement(name = "JWT"),
      description =
          "Lưu ý: Bearer value (JWT) phải là refresh token. Logout nếu đã nhập access token cũ và nhập refresh token vào.",
      tags = {"Auth"})
  @GetMapping("/auth/refresh")
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

  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PutMapping("/staffs/{username}")
  public StaffDTO updateStaff(@RequestBody StaffDTO staffDTO, @PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staffMapper.updateStaffFromStaffDTO(staffDTO, staff);
    //    staffUserService.saveStaff(staff);
    return staffMapper.staffToStaffDTO(staff);
  }

  @Operation(
      summary = "Khóa tài khoản hiện tại",
      security = @SecurityRequirement(name = "JWT"),
      description =
          "Lưu ý: Bearer value (JWT) phải là refresh token. Logout nếu đã nhập access token cũ và nhập refresh token vào.",
      tags = {"Auth"})
  @DeleteMapping("/staffs/disable/{username}")
  public void deleteStaff(@PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staff.setStatus(0);
  }
}
