package com.karaoke.manager.service;

import com.karaoke.manager.entity.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskSchedulerService {

  private final TaskScheduler taskScheduler;
  private final RoomService roomService;

  public void addCancelReservationRoomTask(Long roomBookingId, Date executeTime) {
    taskScheduler.schedule(
        new Runnable() {
          @Override
          public void run() {
            roomService
                .getRoomBookingById(roomBookingId)
                .ifPresent(
                    roomBooking -> {
                      if (roomBooking
                          .getBookingStatus()
                          .getCodeName()
                          .equals(BookingStatus.PENDING)) {
                        roomBooking.setBookingStatus(
                            roomService.getBookingStatusByCodeName(BookingStatus.CANCEL));
                        roomService.updateRoomBooking(roomBooking);
                      }
                    });
          }
        },
        executeTime);
  }
}
