package com.karaoke.manager.api;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.repository.RoleRepository;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.support.DtoConvert;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController @RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final StaffUserService staffUserService;
    private final RoleRepository roleRepository;
    private final DtoConvert dtoConvert;

    @GetMapping("/staffs")
    public ResponseEntity<List<StaffDTO>> getStaffs(){
        List<Staff> staffs =  staffUserService.getStaffs();
        List<StaffDTO> staffDTOS = staffs.stream().map(dtoConvert::staffDTO).collect(Collectors.toList());
        return ResponseEntity.ok(staffDTOS);
    }

//    @GetMapping("/roles")
//    public ResponseEntity<String> getRoles(){
//        return ResponseEntity.ok();
//    }
}
