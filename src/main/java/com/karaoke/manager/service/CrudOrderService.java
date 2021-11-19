package com.karaoke.manager.service;

import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.OrderStatus;
import com.karaoke.manager.entity.ProductOrderedHistory;
import com.karaoke.manager.entity.support.ProductOrderKey;
import com.karaoke.manager.repository.OrderRepository;
import com.karaoke.manager.repository.OrderStatusRepository;
import com.karaoke.manager.repository.ProductOrderedHistoryRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CrudOrderService extends CrudBaseEntityService<Order> implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderStatusRepository orderStatusRepository;
  private final ProductOrderedHistoryRepository productOrderedHistoryRepository;

  protected CrudOrderService(
      JpaRepository<Order, Long> repository,
      OrderRepository orderRepository,
      OrderStatusRepository orderStatusRepository,
      ProductOrderedHistoryRepository productOrderedHistoryRepository) {
    super(repository);
    this.orderRepository = orderRepository;
    this.orderStatusRepository = orderStatusRepository;
    this.productOrderedHistoryRepository = productOrderedHistoryRepository;
  }

  @Override
  public Order getOrderByBookingId(Long bookingId) {
    return orderRepository.findByRoomBooking_Id(bookingId);
  }

  @Override
  public OrderStatus getOrderStatusByCodeName(String codeName) {
    return orderStatusRepository.findByCodeName(codeName);
  }

  @Override
  public Optional<ProductOrderedHistory> getProductOrderedHistory(ProductOrderKey productOrderKey) {
    return productOrderedHistoryRepository.findById(productOrderKey);
  }

  @Override
  public ProductOrderedHistory saveProductOrderedHistory(
      ProductOrderedHistory productOrderedHistory) {
    return productOrderedHistoryRepository.save(productOrderedHistory);
  }

  @Override
  public Page<Order> getOrdersByDay(Date day, Pageable pageable) {
    Timestamp start = new Timestamp(day.getTime());
    Timestamp end = new Timestamp(day.getTime() + ((24 * 60 * 60) - 1) * 1000);
    return orderRepository.findByCreatedAtBetween(start, end, pageable);
  }

  @Override
  public Double revenueAroundTime(Timestamp startTime, Timestamp endTime) {
    List<Order> orders =
        orderRepository.findByCreatedAtBetweenAndStatus_CodeName(
            startTime, endTime, OrderStatus.DONE);
    return orders.stream().mapToDouble(Order::getTotal).sum();
  }
}
