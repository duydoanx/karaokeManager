package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

  private String name;

  private Double price;

  private String description;

  @OneToMany(mappedBy = "product", orphanRemoval = true)
  private List<ProductOrderedHistory> productOrderedHistories;

  //  @OneToMany(mappedBy = "product")
  //  private List<ProductOrderedHistory> productOrderedHistories = new ArrayList<>();
}
