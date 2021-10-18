package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {

  private String name;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private RoomType roomType;

  @OneToMany(mappedBy = "room")
  private List<RoomBooking> roomBookings = new ArrayList<>();
}
