package com.karaoke.manager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {
  private Long id;

  private Long bookingId;

  private Double numberHoursBooked;

  private Double discountPercent;

  private Double discountMoney;

  private Double total;

  private List<ProductOrderedHistoryDTO> products;
}
