package com.karaoke.manager.service;

import com.karaoke.manager.entity.Product;
import com.karaoke.manager.service.base.CrudEntityService;

public interface ProductService extends CrudEntityService<Product> {
  Boolean productIsExist(Long productId);
}
