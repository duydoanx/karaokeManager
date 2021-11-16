package com.karaoke.manager.api;

import com.karaoke.manager.dto.GuestDTO;
import com.karaoke.manager.entity.Guest;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.mapper.GuestMapper;
import com.karaoke.manager.service.GuestService;
import com.karaoke.manager.validation.group.guest.CreateGuest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest-manager")
@RequiredArgsConstructor
public class GuestManagerController {

  private final GuestService guestService;
  private final GuestMapper guestMapper;

  // API thêm mới khách hàng
  @PostMapping("/add")
  public ResponseApi<GuestDTO> addGuest(
      @RequestBody @Validated(CreateGuest.class) GuestDTO guestDTO) {
    Guest guest = guestMapper.guestDTOToGuest(guestDTO);
    guest.setStatus(1);
    Guest save = guestService.save(guest);
    return new ResponseApi<>(HttpStatus.OK.value(), guestMapper.guestToGuestDTO(guest));
  }
}
