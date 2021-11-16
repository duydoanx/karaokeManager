package com.karaoke.manager.service;

import com.karaoke.manager.entity.Product;
import com.karaoke.manager.repository.ProductRepository;
import com.karaoke.manager.service.base.CrudBaseEntityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CrudProductService extends CrudBaseEntityService<Product> implements ProductService {

  private final ProductRepository productRepository;

  protected CrudProductService(
      JpaRepository<Product, Long> repository, ProductRepository productRepository) {
    super(repository);
    this.productRepository = productRepository;
  }

  @Override
  public Boolean productIsExist(Long productId) {
    return productRepository.existsById(productId);
  }
}
