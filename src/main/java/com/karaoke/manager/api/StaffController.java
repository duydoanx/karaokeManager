package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.mapper.StaffMapper;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.validation.group.staff.Create;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staffs")
@Transactional
public class StaffController {
  private final StaffUserService staffUserService;
  private final StaffMapper staffMapper;
  private final PasswordEncoder passwordEncoder;

  @Operation(
      summary = "Lấy danh sách nhân viên",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Thanh cong"),
        @ApiResponse(
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = ResponseApi.class)))
      })
  @PreAuthorize("hasAuthority(@permissionSupport.readStaff())")
  @GetMapping()
  public ResponseApi<ResponsePage> getStaffs(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());
    Page<Staff> staffs = staffUserService.getStaffs(pageable);
    ResponsePage responsePage =
        new ResponsePage(
            staffs.getContent().stream()
                .map(staffMapper::staffToStaffDTO)
                .collect(Collectors.toList()),
            page,
            staffs.getTotalPages());
    return new ResponseApi<>(HttpStatus.OK.value(), responsePage);
  }

  @Operation(
      summary = "Tạo nhân viên mới",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping()
  public ResponseApi<StaffDTO> createStaff(
      @RequestBody @Validated(Create.class) StaffDTO staffDTO) {
    staffDTO.setStatus(Staff.ENABLE);

    return new ResponseApi<>(
        HttpStatus.OK.value(),
        staffMapper.staffToStaffDTO(
            staffUserService.addStaff(staffMapper.staffDTOToStaff(staffDTO))));
  }

  @Operation(
      summary = "Cập nhật tài khoản hiện tại",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/update/{username}")
  public ResponseApi<StaffDTO> updateStaff(
      @RequestBody StaffDTO staffDTO, @PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staffMapper.updateStaffFromStaffDTO(staffDTO, staff);
    return new ResponseApi<>(HttpStatus.OK.value(), staffMapper.staffToStaffDTO(staff));
  }

  @Operation(
      summary = "Khóa tài khoản hiện tại",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/disable/{username}")
  public ResponseApi<?> deleteStaff(@PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staff.setStatus(Staff.DISABLE);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  @GetMapping("/{username}")
  // API lấy thông tin nhân viên theo username
  public ResponseApi<StaffDTO> getStaff(@PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    if (staff == null) {
      throw new RuntimeException("Can not find staff.");
    }
    return new ResponseApi<>(HttpStatus.OK.value(), staffMapper.staffToStaffDTO(staff));
  }

  // API đổi mật khẩu dành cho manager
  @PostMapping("/password/{username}")
  public ResponseApi<?> changePasswordForManager(
      @RequestBody Map<String, String> params, @PathVariable String username) {
    if (!params.containsKey("password")) {
      throw new RuntimeException("Can not find password in request body.");
    }
    String password = params.get("password");
    Staff staff = staffUserService.getStaff(username);
    staff.setPassword(passwordEncoder.encode(password));
    staffUserService.updateStaff(staff);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API đổi mật khẩu dành cho staff
  @PostMapping("/password")
  public ResponseApi<?> changeOwnerPassword(@RequestBody Map<String, String> params) {
    if (!params.containsKey("password")) {
      throw new RuntimeException("Can not find password in request body.");
    }
    String password = params.get("password");
    String username =
        ((Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getUsername();
    Staff staff = staffUserService.getStaff(username);
    staff.setPassword(passwordEncoder.encode(password));
    staffUserService.updateStaff(staff);
    return new ResponseApi<>(HttpStatus.OK.value());
  }
}
