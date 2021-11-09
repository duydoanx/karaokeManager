package com.karaoke.manager.service;

import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.OrderStatus;
import com.karaoke.manager.service.base.CrudEntityService;

public interface OrderService extends CrudEntityService<Order> {
  Order getOrderByBookingId(Long bookingId);

  OrderStatus getOrderStatusByCodeName(String codeName);
}
