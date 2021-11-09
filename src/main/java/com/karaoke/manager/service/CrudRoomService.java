package com.karaoke.manager.service;

import com.karaoke.manager.entity.BookingStatus;
import com.karaoke.manager.entity.Room;
import com.karaoke.manager.entity.RoomBooking;
import com.karaoke.manager.entity.RoomType;
import com.karaoke.manager.repository.BookingStatusRepository;
import com.karaoke.manager.repository.RoomBookingRepository;
import com.karaoke.manager.repository.RoomRepository;
import com.karaoke.manager.repository.RoomTypeRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrudRoomService extends CrudBaseEntityService<Room> implements RoomService {

  private final RoomRepository roomRepository;
  private final RoomTypeRepository roomTypeRepository;
  private final RoomBookingRepository roomBookingRepository;
  private final BookingStatusRepository bookingStatusRepository;

  public CrudRoomService(
      RoomRepository repository,
      RoomTypeRepository roomTypeRepository,
      RoomBookingRepository roomBookingRepository,
      BookingStatusRepository bookingStatusRepository) {
    super(repository);
    this.roomRepository = repository;
    this.roomTypeRepository = roomTypeRepository;
    this.roomBookingRepository = roomBookingRepository;
    this.bookingStatusRepository = bookingStatusRepository;
  }

  @Override
  public Room getEnableRoom(Long id) {
    return roomRepository.findByIdAndStatusCode(id, Room.ENABLE);
  }

  @Override
  public RoomType getRoomTypeByCodeName(String codeName) {
    return roomTypeRepository.findByCodeName(codeName);
  }

  @Override
  public BookingStatus getBookingStatusByCodeName(String codeName) {
    return bookingStatusRepository.findByCodeName(codeName);
  }

  @Override
  public List<Room> getRooms() {
    return roomRepository.findAll();
  }

  @Override
  public RoomBooking addRoomBooking(RoomBooking roomBooking) {
    return roomBookingRepository.save(roomBooking);
  }

  @Override
  public void updateRoomBooking(RoomBooking roomBooking) {
    roomBookingRepository.save(roomBooking);
  }

  @Override
  public Optional<RoomBooking> getRoomBookingById(Long id) {
    return roomBookingRepository.findById(id);
  }

  @Override
  public List<RoomBooking> getReservationRooms() {
    return roomBookingRepository.findByBookingStatus_CodeName(BookingStatus.PENDING);
  }

  @Override
  public List<RoomBooking> getRoomBookingByBetweenTimeAndRoomId(
      Timestamp startTime, Timestamp endTime, Long roomId) {
    return roomBookingRepository.findByStartTimeIsBetweenAndRoom_Id(startTime, endTime, roomId);
  }

  @Override
  public List<RoomBooking> getRoomBookingAroundTime(
      Timestamp startTime, Timestamp endTime, Long roomId) {
    List<RoomBooking> roomBookings =
        getRoomBookingByBetweenTimeAndRoomId(startTime, endTime, roomId);
    List<String> var1 = Arrays.asList(BookingStatus.PENDING, BookingStatus.BOOKED);
    return roomBookings.stream()
        .filter(roomBooking -> var1.contains(roomBooking.getBookingStatus().getCodeName()))
        .collect(Collectors.toList());
  }

  @Override
  public Boolean isExistRoomBookingByBetweenTimeAndRoomId(
      Timestamp startTime, Timestamp endTime, Long roomId) {
    return roomBookingRepository.existsByStartTimeBetweenAndRoom_Id(startTime, endTime, roomId);
  }

  @Override
  public RoomBooking saveRoomBooking(RoomBooking roomBooking) {
    return roomBookingRepository.save(roomBooking);
  }

  @Override
  public Boolean roomIsAvailable(Long roomId) {
    return !roomBookingRepository.existsByBookingStatus_CodeNameAndRoom_Id(
        BookingStatus.BOOKED, roomId);
  }

  @Override
  public Optional<RoomBooking> getCurrentBookedByRoomId(Long roomId) {
    List<RoomBooking> roomBookings =
        roomBookingRepository.findByBookingStatus_CodeNameAndRoom_Id(BookingStatus.BOOKED, roomId);

    return Optional.ofNullable(roomBookings.isEmpty() ? null : roomBookings.get(0));
  }

  @Override
  public List<RoomBooking> getRoomBookingByRoomId(Long roomId) {
    return roomBookingRepository.findByRoom_Id(roomId);
  }
}
