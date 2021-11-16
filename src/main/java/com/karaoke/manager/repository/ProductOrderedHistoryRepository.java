package com.karaoke.manager.repository;

import com.karaoke.manager.entity.ProductOrderedHistory;
import com.karaoke.manager.entity.support.ProductOrderKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOrderedHistoryRepository
    extends JpaRepository<ProductOrderedHistory, ProductOrderKey> {

  @Override
  Optional<ProductOrderedHistory> findById(ProductOrderKey productOrderKey);
}
