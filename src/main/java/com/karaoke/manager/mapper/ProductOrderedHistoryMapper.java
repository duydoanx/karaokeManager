package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.ProductOrderedHistoryDTO;
import com.karaoke.manager.entity.ProductOrderedHistory;
import com.karaoke.manager.repository.ProductRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {ProductRepository.class})
public abstract class ProductOrderedHistoryMapper {

  @Autowired protected ProductRepository productRepository;

  @Mapping(
      target = "name",
      expression =
          "java(productRepository.findById(productOrderedHistory.getId().getProductId()).get().getName())")
  public abstract ProductOrderedHistoryDTO productOrderedHistoryToProductOrderedHistoryDTO(
      ProductOrderedHistory productOrderedHistory);
}
