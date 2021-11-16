package com.karaoke.manager.entity.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOrderKey implements Serializable {

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "order_id")
  private Long orderId;
}
