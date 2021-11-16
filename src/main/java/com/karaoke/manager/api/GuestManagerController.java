package com.karaoke.manager.api;

import com.karaoke.manager.dto.GuestDTO;
import com.karaoke.manager.entity.Guest;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.mapper.GuestMapper;
import com.karaoke.manager.service.GuestService;
import com.karaoke.manager.validation.group.guest.CreateGuest;
import com.karaoke.manager.validation.group.guest.UpdateGuest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guests-manager")
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

  // API cập nhật khách hàng bằng số điện thoại
  @PostMapping("/update/{phoneNumber}")
  public ResponseApi<?> updateGuest(
      @RequestBody @Validated(UpdateGuest.class) GuestDTO guestDTO,
      @PathVariable String phoneNumber) {
    Guest guest = guestService.getGuestByPhoneNumber(phoneNumber);
    if (guest == null) {
      throw new RuntimeException("Unable to find guest.");
    }
    guestMapper.updateGuestFromGuestDTO(guestDTO, guest);
    guestService.save(guest);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API vô hiệu hoá khách hàng
  @GetMapping("/disable/{phoneNumber}")
  public ResponseApi<?> disableGuest(@PathVariable String phoneNumber) {
    Guest guest = guestService.getGuestByPhoneNumber(phoneNumber);
    if (guest == null) {
      throw new RuntimeException("Unable to find guest.");
    }

    guest.setStatus(Guest.DISABLE);
    guestService.save(guest);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API kích hoạt khách hàng
  @GetMapping("/enable/{phoneNumber}")
  public ResponseApi<?> enableGuest(@PathVariable String phoneNumber) {
    Guest guest = guestService.getGuestByPhoneNumber(phoneNumber);
    if (guest == null) {
      throw new RuntimeException("Unable to find guest.");
    }
    if (guest.getStatus().equals(Guest.ENABLE)) {
      throw new RuntimeException("The guest has not been disabled.");
    }

    guest.setStatus(Guest.ENABLE);
    guestService.save(guest);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API xem tất cả guest
  @GetMapping
  public ResponseApi<ResponsePage> getAllGuest(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());
    Page<Guest> guests = guestService.findAll(pageable);
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        new ResponsePage(
            guests.getContent().stream()
                .map(guestMapper::guestToGuestDTO)
                .collect(Collectors.toList()),
            page,
            guests.getTotalPages()));
  }

  // API xem từng guest theo số điện thoại
  @GetMapping("/phone-number/{phoneNumber}")
  public ResponseApi<GuestDTO> getGuestByPhoneNumber(@PathVariable String phoneNumber) {
    Guest guest = guestService.getGuestByPhoneNumber(phoneNumber);
    if (guest == null) {
      throw new RuntimeException("Unable to find guest.");
    }
    return new ResponseApi<>(HttpStatus.OK.value(), guestMapper.guestToGuestDTO(guest));
  }

  // API xem từng guest theo id
  @GetMapping("/id/{guestId}")
  public ResponseApi<GuestDTO> getGuestById(@PathVariable Long guestId) {
    Optional<Guest> guestOptional = guestService.findById(guestId);
    if (!guestOptional.isPresent()) {
      throw new RuntimeException("Unable to find guest.");
    }
    return new ResponseApi<>(
        HttpStatus.OK.value(), guestMapper.guestToGuestDTO(guestOptional.get()));
  }
}
