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

  public static final String ENABLE = "ENABLE";
  public static final String DISABLE = "DISABLE";

  @Column(unique = true)
  private String name;

  @Column(name = "status_code", columnDefinition = "varchar(255) default 'ENABLE'")
  private String statusCode;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private RoomType roomType;

  @OneToMany(mappedBy = "room")
  private List<RoomBooking> roomBookings = new ArrayList<>();
}
