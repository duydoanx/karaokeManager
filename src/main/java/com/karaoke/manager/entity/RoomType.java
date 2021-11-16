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
@Table(name = "room_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomType extends BaseEntity {
  public static final String NORMAL = "NORMAL";
  public static final String VIP = "VIP";

  private String name;

  @Column(name = "code_name", unique = true)
  private String codeName;

  private Double price;

  @OneToMany(mappedBy = "roomType")
  private List<Room> rooms = new ArrayList<>();
}
