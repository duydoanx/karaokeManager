package com.karaoke.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

  @Column(name = "phone_number")
  private String phoneNumber;

  private String email;

  @OneToMany(mappedBy = "staff")
  private List<RoomBooking> roomBookings = new ArrayList<>();

  @JsonManagedReference
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;
}
