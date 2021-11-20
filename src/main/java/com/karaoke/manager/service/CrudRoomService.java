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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  public Page<Room> getEnableRooms(Pageable pageable) {
    return roomRepository.findByStatusCode(Room.ENABLE, pageable);
  }

  @Override
  public Room getDisableRoom(Long id) {
    return roomRepository.findByIdAndStatusCode(id, Room.DISABLE);
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
  public Page<Room> getRooms(Pageable pageable) {
    return roomRepository.findAll(pageable);
  }

  @Override
  public Page<Room> getEmptyRoomsAroundTime(
      Timestamp startTime, Timestamp endTime, Pageable pageable) {
    return roomRepository.findEmptyRooms(
        BookingStatus.BOOKED, BookingStatus.PENDING, startTime, endTime, pageable);
  }

  @Override
  public Page<Room> getBookedRoom(Pageable pageable) {
    return roomRepository.findByRoomBookings_BookingStatus_CodeName(BookingStatus.BOOKED, pageable);
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
  public List<RoomBooking> getBusyRoomBookingAroundTime(
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
  public Page<RoomBooking> getRoomBookingByGuestPhoneNumber(String phoneNumber, Pageable pageable) {
    return roomBookingRepository.findByGuest_PhoneNumber(phoneNumber, pageable);
  }

  @Override
  public Page<RoomBooking> getRoomBookingByGuestId(Long guestId, Pageable pageable) {
    return roomBookingRepository.findByGuest_Id(guestId, pageable);
  }

  @Override
  public Boolean roomIsAvailable(Long roomId) {
    return !roomBookingRepository.existsByBookingStatus_CodeNameAndRoom_Id(
        BookingStatus.BOOKED, roomId);
  }

  @Override
  public Optional<RoomBooking> getCurrentBookedByRoomId(Long roomId) {
    Pageable pageable = PageRequest.of(0, 1);
    Page<RoomBooking> roomBookings =
        roomBookingRepository.findByBookingStatus_CodeNameAndRoom_Id(
            BookingStatus.BOOKED, roomId, pageable);

    return Optional.ofNullable(roomBookings.isEmpty() ? null : roomBookings.getContent().get(0));
  }

  @Override
  public Page<RoomBooking> getRoomBookingByRoomId(Long roomId, Pageable pageable) {
    return roomBookingRepository.findByRoom_Id(roomId, pageable);
  }

  @Override
  public Page<RoomBooking> getRoomBookingByRoomIdAndBookingStatus(
      Long roomId, String bookingStatus, Pageable pageable) {
    return roomBookingRepository.findByBookingStatus_CodeNameAndRoom_Id(
        bookingStatus, roomId, pageable);
  }

  @Override
  public Page<RoomType> getRoomTypes(Pageable pageable) {
    return roomTypeRepository.findAll(pageable);
  }

  @Override
  public RoomType saveRoomType(RoomType roomType) {
    return roomTypeRepository.save(roomType);
  }
}
