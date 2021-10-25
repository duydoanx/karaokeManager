package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.mapper.StaffMapper;
import com.karaoke.manager.service.StaffUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
  @PreAuthorize("hasAuthority(@permissionSupport.readStaff())")
  @GetMapping("/staffs")
  public List<StaffDTO> getStaffs() {
    List<Staff> staffs = staffUserService.getStaffs();
    return staffs.stream().map(staffMapper::staffToStaffDTO).collect(Collectors.toList());
  }

  @Operation(
      summary = "Tạo nhân viên mới",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/staffs")
  public StaffDTO createStaff(@RequestBody StaffDTO staffDTO) {
    return staffMapper.staffToStaffDTO(
        staffUserService.saveStaff(staffMapper.staffDTOToStaff(staffDTO)));
  }

  @PreAuthorize("hasAuthority(@permissionSupport.writeStaff())")
  @PostMapping("/staffs/update/{username}")
  public StaffDTO updateStaff(@RequestBody StaffDTO staffDTO, @PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staffMapper.updateStaffFromStaffDTO(staffDTO, staff);
    return staffMapper.staffToStaffDTO(staff);
  }

  @Operation(
      summary = "Khóa tài khoản hiện tại",
      security = @SecurityRequirement(name = "JWT"),
      tags = {"Staffs"})
  @PostMapping("/staffs/disable/{username}")
  public void deleteStaff(@PathVariable String username) {
    Staff staff = staffUserService.getStaff(username);
    staff.setStatus(0);
  }
}
