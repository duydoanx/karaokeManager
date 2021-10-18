package com.karaoke.manager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
// @EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at")
  // @CreatedDate
  private Timestamp createdAt;

  @Column(name = "created_by")
  // @CreatedBy
  private String createdBy;

  @Column(name = "modified_at")
  // @LastModifiedDate
  private Timestamp modifiedAt;

  @Column(name = "modified_by")
  // @LastModifiedBy
  private String modifiedBy;
}
