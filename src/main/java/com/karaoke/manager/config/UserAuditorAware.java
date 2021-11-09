package com.karaoke.manager.config;

import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.service.StaffUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserAuditorAware implements AuditorAware<Staff> {

  @Autowired private StaffUserService staffUserService;

  @Override
  public Optional<Staff> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return Optional.of((Staff) authentication.getPrincipal());
    }

    return Optional.empty();
  }
}
