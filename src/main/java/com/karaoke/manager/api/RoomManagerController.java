package com.karaoke.manager.api;

import com.karaoke.manager.dto.RoomDTO;
import com.karaoke.manager.entity.BookingStatus;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.entity.RoomType;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.mapper.RoomMapper;
import com.karaoke.manager.service.RoomService;
import com.karaoke.manager.validation.group.room.CreateRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/rooms-manager")
@RestController
@RequiredArgsConstructor
public class RoomManagerController {

  private final RoomService roomService;
  private final RoomMapper roomMapper;

  // API lấy danh sách tất cả các phòng
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseApi<ResponsePage> getAllRoom(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());
    Page<Room> rooms = roomService.getRooms(pageable);
    ResponsePage responsePage =
        new ResponsePage(
            rooms.getContent().stream().map(roomMapper::roomToRoomDTO).collect(Collectors.toList()),
            page,
            rooms.getTotalPages());
    return new ResponseApi<>(HttpStatus.OK.value(), responsePage);
  }

  // API xem danh sách phòng trống trong khoảng thời gian từ a -> b
  @PostMapping("/empty-room")
  public ResponseApi<ResponsePage> getEmptyRoomAroundTime(
      @RequestBody Map<String, Date> dateMap,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {

    if (!dateMap.containsKey("startTime")) {
      throw new RuntimeException("Missing start time.");
    }
    if (!dateMap.containsKey("endTime")) {
      throw new RuntimeException("Missing end time.");
    }
    Timestamp startTime = new Timestamp(dateMap.get("startTime").getTime());
    Timestamp endTime = new Timestamp(dateMap.get("endTime").getTime());
    if (!startTime.before(endTime) && !startTime.equals(endTime)) {
      throw new RuntimeException("The start time must be before or equal end time.");
    }

    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());

    Page<Room> emptyRoomsAroundTime =
        roomService.getEmptyRoomsAroundTime(startTime, endTime, pageable);
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        new ResponsePage(
            emptyRoomsAroundTime.getContent().stream()
                .map(roomMapper::roomToRoomDTO)
                .collect(Collectors.toList()),
            page,
            emptyRoomsAroundTime.getTotalPages()));
  }

  // API thêm phòng mới
  @PostMapping("/add")
  public ResponseApi<Map<String, Long>> addRoom(
      @RequestBody @Validated(CreateRoom.class) RoomDTO roomDTO) {
    roomDTO.setStatusCode(Room.ENABLE);
    Room room = roomService.save(roomMapper.roomDTOToRoom(roomDTO));
    return new ResponseApi<>(HttpStatus.OK.value(), Collections.singletonMap("id", room.getId()));
  }

  // API vô hiệu hoá phòng
  @GetMapping("/disable/{roomId}")
  public ResponseApi<?> disableRoom(@PathVariable Long roomId) {
    Optional<Room> optionalRoom = roomService.findById(roomId);
    if (!optionalRoom.isPresent()) {
      throw new RuntimeException("Can not find the room.");
    }
    Room room = optionalRoom.get();
    if (room.getStatusCode().equals(Room.DISABLE)) {
      throw new RuntimeException("The room has been disabled.");
    }

    room.setStatusCode(Room.DISABLE);
    roomService.save(room);

    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API kích hoạt lại phòng đã vô hiệu hoá
  @GetMapping("/enable/{roomId}")
  public ResponseApi<?> enableRoom(@PathVariable Long roomId) {
    Room disableRoom = roomService.getDisableRoom(roomId);
    if (disableRoom == null) {
      throw new RuntimeException("Can not find the room or the room does not disable.");
    }
    disableRoom.setStatusCode(Room.ENABLE);
    roomService.save(disableRoom);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API sửa thông tin phòng
  @PostMapping("/update/{roomId}")
  public ResponseApi<?> updateRoom(@PathVariable Long roomId, @RequestBody RoomDTO roomDTO) {
    List<String> status = Arrays.asList(Room.ENABLE, Room.DISABLE);
    if (roomDTO.getStatusCode() != null && !status.contains(roomDTO.getStatusCode())) {
      throw new RuntimeException(
          "Status code must be " + Room.ENABLE + " or " + Room.DISABLE + ".");
    }
    List<String> types = Arrays.asList(RoomType.NORMAL, RoomType.VIP);
    if (roomDTO.getType() != null && !types.contains(roomDTO.getType())) {
      throw new RuntimeException(
          "Room type must be " + RoomType.NORMAL + " or " + RoomType.VIP + ".");
    }

    Optional<Room> roomOptional = roomService.findById(roomId);
    if (!roomOptional.isPresent()) {
      throw new RuntimeException("Can not find the room.");
    }
    Room room = roomOptional.get();
    roomMapper.updateRoomFromRoomDTO(roomDTO, room);
    roomService.save(room);

    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API lấy danh sách phòng khả dụng
  @GetMapping("/enable")
  public ResponseApi<?> getAllEnableRooms(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());

    Page<Room> enableRooms = roomService.getEnableRooms(pageable);
    List<RoomDTO> content = enableRooms.map(roomMapper::roomToRoomDTO).getContent();
    return new ResponseApi<>(
        HttpStatus.OK.value(), new ResponsePage(content, page, enableRooms.getTotalPages()));
  }

  // API xem thông tin phòng theo mã phòng
  @GetMapping("/{roomId}")
  public ResponseApi<?> getRoomById(@PathVariable Long roomId) {
    Optional<Room> roomOptional = roomService.findById(roomId);
    if (!roomOptional.isPresent()) {
      throw new RuntimeException("Can not find the room.");
    }
    return new ResponseApi<>(HttpStatus.OK.value(), roomMapper.roomToRoomDTO(roomOptional.get()));
  }

  // API xem danh sách phòng đang được dùng.
  @GetMapping("/booked")
  public ResponseApi<ResponsePage> getAllBookedRoom(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());

    Page<Room> bookedRooms = roomService.getBookedRoom(pageable);

    return new ResponseApi<>(
        HttpStatus.OK.value(),
        new ResponsePage(
            bookedRooms.getContent().stream()
                .map(roomMapper::roomToRoomDTO)
                .collect(Collectors.toList()),
            page,
            bookedRooms.getTotalPages()));
  }

  // API Kiểm tra xem phòng có đang được sử dụng hay không
  @GetMapping("/is-booked/{roomId}")
  public ResponseApi<Map<String, Boolean>> isBooked(@PathVariable Long roomId) {
    Room enableRoom = roomService.getEnableRoom(roomId);
    if (enableRoom == null) {
      throw new RuntimeException("The room does not exist or disable.");
    }
    Optional<RoomBooking> bookingOptional =
        enableRoom.getRoomBookings().stream()
            .filter(
                roomBooking ->
                    roomBooking.getBookingStatus().getCodeName().equals(BookingStatus.BOOKED))
            .findFirst();
    return new ResponseApi<>(
        HttpStatus.OK.value(), Collections.singletonMap("isBooked", bookingOptional.isPresent()));
  }
}
