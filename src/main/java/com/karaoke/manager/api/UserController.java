package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.mapper.StaffMapper;
import com.karaoke.manager.service.StaffUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Transactional
public class UserController {
  private final StaffUserService staffUserService;
  private final StaffMapper staffMapper;

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
  @GetMapping("/staffs")
  public ResponseApi<List<StaffDTO>> getStaffs() {
    List<Staff> staffs = staffUserService.getStaffs();
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        staffs.stream().map(staffMapper::staffToStaffDTO).collect(Collectors.toList()));
  }

  @Operation(
      summary = "Tạo nhân viên mới",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/staffs")
  public ResponseApi<StaffDTO> createStaff(@RequestBody StaffDTO staffDTO) {
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        staffMapper.staffToStaffDTO(
            staffUserService.saveStaff(staffMapper.staffDTOToStaff(staffDTO))));
  }

  @Operation(
      summary = "Cập nhật tài khoản hiện tại",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/staffs/update/{username}")
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
  @PostMapping("/staffs/disable/{username}")
  public ResponseApi<?> deleteStaff(@PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staff.setStatus(0);
    return new ResponseApi<>(HttpStatus.OK.value());
  }
}
