package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking_status")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BookingStatus extends BaseEntity{

    @Column(name = "code_name")
    private String codeName;

    private String description;

    @OneToMany(mappedBy = "bookingStatus")
    private List<RoomBooking> bookings = new ArrayList<>();
}
