package com.karaoke.manager.service.base;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class CrudBaseEntityService<E> implements CrudEntityService<E> {

  private final JpaRepository<E, Long> repository;

  protected CrudBaseEntityService(JpaRepository<E, Long> repository) {
    this.repository = repository;
  }

  @Override
  public Optional<E> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public E save(E entity) {
    return repository.save(entity);
  }

  @Override
  public void delete(Long id) {
    Optional<E> entity = repository.findById(id);
    entity.ifPresent(repository::delete);
  }
}
