package com.karaoke.manager.api;

import com.karaoke.manager.dto.RoomTypeDTO;
import com.karaoke.manager.entity.RoomType;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.mapper.RoomTypeMapper;
import com.karaoke.manager.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

  private final RoomService roomService;
  private final RoomTypeMapper roomTypeMapper;

  @GetMapping
  public ResponseApi<List<RoomTypeDTO>> getRoomTypes() {
    Page<RoomType> roomTypes = roomService.getRoomTypes(PageRequest.of(0, 2));

    return new ResponseApi<>(
        HttpStatus.OK.value(),
        roomTypes.getContent().stream()
            .map(roomTypeMapper::roomTypeToRoomTypeDTO)
            .collect(Collectors.toList()));
  }

  @PostMapping("/price")
  public ResponseApi<RoomTypeDTO> setPrice(@RequestBody Map<String, String> map) {
    if (!map.containsKey("type")) {
      throw new RuntimeException("Unable to find type.");
    }
    RoomType roomType = roomService.getRoomTypeByCodeName(map.get("type"));

    if (!map.containsKey("price")) {
      throw new RuntimeException("Unable to find price.");
    }

    double price;
    try {
      price = Double.parseDouble(map.get("price"));
    } catch (NumberFormatException e) {
      throw new RuntimeException("The price is not in the correct format.");
    }

    if (roomType == null) {
      throw new RuntimeException("Unable to find type.");
    }

    roomType.setPrice(price);
    roomType = roomService.saveRoomType(roomType);

    return new ResponseApi<>(HttpStatus.OK.value(), roomTypeMapper.roomTypeToRoomTypeDTO(roomType));
  }

  @GetMapping("/{roomTypeCodeName}")
  public ResponseApi<RoomTypeDTO> getRoomType(@PathVariable String roomTypeCodeName) {
    RoomType roomTypeByCodeName = roomService.getRoomTypeByCodeName(roomTypeCodeName);
    if (roomTypeByCodeName == null) {
      throw new RuntimeException("Unable to find room type.");
    }

    return new ResponseApi<>(
        HttpStatus.OK.value(), roomTypeMapper.roomTypeToRoomTypeDTO(roomTypeByCodeName));
  }
}
