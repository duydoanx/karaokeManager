package com.karaoke.manager.support;

import com.karaoke.manager.dto.StaffDTO;
import com.karaoke.manager.entity.Role;
import com.karaoke.manager.entity.Staff;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoConvert {

  private final ModelMapper modelMapper;

  public StaffDTO staffDTO(Staff staff) {
    StaffDTO staffDTO = modelMapper.map(staff, StaffDTO.class);

    return staffDTO;
  }
}
