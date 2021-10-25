package com.karaoke.manager.service.base;

import java.util.Optional;

public interface CrudEntityService<E> {
  Optional<E> findById(Long id);

  E save(E entity);

  void delete(Long id);
}
