package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "order_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus extends BaseEntity {
  public static final String PENDING = "PENDING";
  public static final String DONE = "DONE";
  public static final String CANCEL = "CANCEL";

  @Column(name = "code_name")
  private String codeName;

  private String description;

  @OneToMany(mappedBy = "status")
  private List<Order> orders;
}
