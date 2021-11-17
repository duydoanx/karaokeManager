package com.karaoke.manager.dto;

import com.karaoke.manager.validation.group.product.CreateProduct;
import com.karaoke.manager.validation.group.product.UpdateProduct;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ProductDTO {

  private Long id;

  @NotBlank(groups = {CreateProduct.class})
  private String name;

  @NotNull(groups = {CreateProduct.class})
  private Double price;

  @NotBlank(groups = {CreateProduct.class})
  private String description;

  @NotNull(groups = {CreateProduct.class})
  @Max(
      value = 1,
      groups = {CreateProduct.class, UpdateProduct.class},
      message = "Status must be 0 or 1.")
  @Min(
      value = 0,
      groups = {CreateProduct.class, UpdateProduct.class},
      message = "Status must be 0 or 1.")
  private Integer status;
}
