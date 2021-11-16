package com.karaoke.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderedHistoryDTO {

  private String name;

  private Double price;

  private Integer quantity;
}
