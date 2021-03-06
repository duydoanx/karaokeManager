package com.karaoke.manager.entity;

import com.karaoke.manager.entity.support.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest extends BaseEntity {

  public static final Integer ENABLE = 1;
  public static final Integer DISABLE = 0;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Enumerated(EnumType.ORDINAL)
  private Gender gender;

  private String address1;

  private String address2;

  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @Column(unique = true)
  private String email;

  private Integer status;

  @OneToMany(mappedBy = "guest")
  private List<RoomBooking> roomBookings = new ArrayList<>();
}
