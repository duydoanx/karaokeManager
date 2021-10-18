package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room_bookings")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class RoomBooking extends BaseEntity{

    @Column(name = "start_time")
    private Timestamp startTime;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private BookingStatus bookingStatus;

    @OneToMany(mappedBy = "roomBooking")
    private List<Order> orders = new ArrayList<>();
}
