package com.karaoke.manager.entity.support;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProductOrderKey implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_id")
    private Long orderId;
}
