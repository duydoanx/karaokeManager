package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.OrderDTO;
import com.karaoke.manager.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {ProductOrderedHistoryMapper.class})
public abstract class OrderMapper {

  @Autowired protected ProductOrderedHistoryMapper productOrderedHistoryMapper;

  @Mapping(
      target = "products",
      expression =
          "java(order.getProductOrderedHistories().stream().map(productOrderedHistoryMapper::productOrderedHistoryToProductOrderedHistoryDTO).collect(java.util.stream.Collectors.toList()))")
  @Mapping(target = "bookingId", expression = "java(order.getRoomBooking().getId())")
  @Mapping(
      target = "total",
      expression = "java(java.math.BigDecimal.valueOf(order.getTotal()).toPlainString())")
  public abstract OrderDTO orderToOrderDTO(Order order);
}
