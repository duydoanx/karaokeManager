package com.karaoke.manager.mapper;

import com.karaoke.manager.dto.ProductDTO;
import com.karaoke.manager.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

  @Mapping(target = "productOrderedHistories", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  public abstract Product productDTOToProduct(ProductDTO productDTO);

  public abstract ProductDTO productToProductDTO(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "productOrderedHistories", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  @Mapping(target = "modifiedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  public abstract void updateProductFromProductDTO(
      ProductDTO productDTO, @MappingTarget Product product);
}
