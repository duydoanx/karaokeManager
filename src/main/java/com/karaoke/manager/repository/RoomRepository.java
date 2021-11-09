package com.karaoke.manager.repository;

import com.karaoke.manager.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
  Room findByIdAndStatusCode(Long id, String statusCode);
}
