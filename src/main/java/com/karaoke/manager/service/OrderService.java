package com.karaoke.manager.service;

import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.OrderStatus;
import com.karaoke.manager.entity.ProductOrderedHistory;
import com.karaoke.manager.entity.support.ProductOrderKey;
import com.karaoke.manager.service.base.CrudEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface OrderService extends CrudEntityService<Order> {
  Order getOrderByBookingId(Long bookingId);

  OrderStatus getOrderStatusByCodeName(String codeName);

  Optional<ProductOrderedHistory> getProductOrderedHistory(ProductOrderKey productOrderKey);

  ProductOrderedHistory saveProductOrderedHistory(ProductOrderedHistory productOrderedHistory);

  Page<Order> getOrdersByDay(Date date, Pageable pageable);
}
