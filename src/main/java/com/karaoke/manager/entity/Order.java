package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Column(name = "number_hours_booked")
    private Double numberHoursBooked;

    @Column(name = "discount_percent")
    private Double discountPercent;

    @Column(name = "discount_money")
    private Double discountMoney;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private RoomBooking roomBooking;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus status;
}
