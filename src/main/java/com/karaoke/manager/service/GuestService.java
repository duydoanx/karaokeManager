package com.karaoke.manager.service;

import com.karaoke.manager.entity.Guest;
import com.karaoke.manager.service.base.CrudEntityService;

import java.util.Optional;

public interface GuestService extends CrudEntityService<Guest> {
  Guest getGuestByPhoneNumber(String phoneNumber);

  Optional<Guest> getGuestByEmail(String email);

  Guest getGuestPhoneNumberForMapper(String phoneNumber);
}
