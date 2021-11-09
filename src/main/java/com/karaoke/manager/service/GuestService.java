package com.karaoke.manager.service;

import com.karaoke.manager.entity.Guest;
import com.karaoke.manager.service.base.CrudEntityService;

public interface GuestService extends CrudEntityService<Guest> {
  Guest getGuestByPhoneNumber(String phoneNumber);
}
