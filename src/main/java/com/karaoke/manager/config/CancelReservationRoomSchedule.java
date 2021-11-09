package com.karaoke.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class CancelReservationRoomSchedule {

  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();
    poolTaskScheduler.setPoolSize(5);
    poolTaskScheduler.setThreadNamePrefix("TaskScheduler");
    return poolTaskScheduler;
  }
}
