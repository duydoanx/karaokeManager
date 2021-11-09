package com.karaoke.manager.config;

import com.karaoke.manager.entity.Staff;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class AuditorConfig {
  @Bean
  public AuditorAware<Staff> auditorAware() {
    return new UserAuditorAware();
  }
}
