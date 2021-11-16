package com.karaoke.manager.entity;

import com.karaoke.manager.entity.support.ProductOrderKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_ordered_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderedHistory {

  @EmbeddedId private ProductOrderKey id;

  private String description;

  private Integer quantity;

  private Double price;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @MapsId("orderId")
  @JoinColumn(name = "order_id")
  private Order order;
}
