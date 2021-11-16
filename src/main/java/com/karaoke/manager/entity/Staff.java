package com.karaoke.manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.karaoke.manager.entity.support.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
    name = "staff-with-roles",
    attributeNodes = {@NamedAttributeNode(value = "role", subgraph = "role-with-permissions")},
    subgraphs = {
      @NamedSubgraph(
          name = "role-with-permissions",
          attributeNodes = {@NamedAttributeNode(value = "permissions")})
    })
@Entity
@Table(name = "staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends BaseEntity {
  public static final Integer ENABLE = 1;
  public static final Integer DISABLE = 0;

  @Column(unique = true)
  private String username;

  private String password;

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

  @OneToMany(mappedBy = "staff")
  private List<RoomBooking> roomBookings = new ArrayList<>();

  @JsonManagedReference
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  public Staff(
      String username,
      String password,
      String firstName,
      String lastName,
      Gender gender,
      String address1,
      String address2,
      String phoneNumber,
      String email,
      Integer status) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.address1 = address1;
    this.address2 = address2;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.status = status;
  }
}
