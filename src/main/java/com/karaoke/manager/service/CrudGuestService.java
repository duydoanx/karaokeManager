package com.karaoke.manager.service;

import com.karaoke.manager.entity.Guest;
import com.karaoke.manager.repository.GuestRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrudGuestService extends CrudBaseEntityService<Guest> implements GuestService {

  private final GuestRepository guestRepository;

  protected CrudGuestService(GuestRepository repository) {
    super(repository);
    this.guestRepository = repository;
  }

  @Override
  public Guest getGuestByPhoneNumber(String phoneNumber) {
    return guestRepository.findByPhoneNumber(phoneNumber).orElse(null);
  }

  @Override
  public Optional<Guest> getGuestByEmail(String email) {
    return guestRepository.findByEmail(email);
  }

  @Override
  public Guest getGuestPhoneNumberForMapper(String phoneNumber) {
    return guestRepository
        .findByPhoneNumber(phoneNumber)
        .orElseThrow(() -> new RuntimeException("Phone number not exist!"));
  }
}
