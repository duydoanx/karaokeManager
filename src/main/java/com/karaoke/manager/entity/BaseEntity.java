package com.karaoke.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(name = "created_at")
  @CreatedDate
  @JsonIgnore
  private Timestamp createdAt;

  @ManyToOne
  @JoinColumn(name = "created_by")
  @CreatedBy
  @JsonIgnore
  private Staff createdBy;

  @Column(name = "modified_at")
  @LastModifiedDate
  @JsonIgnore
  private Timestamp modifiedAt;

  @ManyToOne
  @JoinColumn(name = "modified_by")
  @LastModifiedBy
  @JsonIgnore
  private Staff modifiedBy;
}
