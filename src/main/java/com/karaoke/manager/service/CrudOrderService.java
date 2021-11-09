package com.karaoke.manager.service;

import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.OrderStatus;
import com.karaoke.manager.repository.OrderRepository;
import com.karaoke.manager.repository.OrderStatusRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CrudOrderService extends CrudBaseEntityService<Order> implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderStatusRepository orderStatusRepository;

  protected CrudOrderService(
      JpaRepository<Order, Long> repository,
      OrderRepository orderRepository,
      OrderStatusRepository orderStatusRepository) {
    super(repository);
    this.orderRepository = orderRepository;
    this.orderStatusRepository = orderStatusRepository;
  }

  @Override
  public Order getOrderByBookingId(Long bookingId) {
    return orderRepository.findByRoomBooking_Id(bookingId);
  }

  @Override
  public OrderStatus getOrderStatusByCodeName(String codeName) {
    return orderStatusRepository.findByCodeName(codeName);
  }
}
