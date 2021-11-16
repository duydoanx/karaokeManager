package com.karaoke.manager.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrudEntityService<E> {
  Optional<E> findById(Long id);

  Page<E> findAll(Pageable pageable);

  E save(E entity);

  void delete(Long id);
}
