package com.karaoke.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

  public static final String MANAGER = "MANAGER";
  public static final String STAFF = "STAFF";
  public static final String ACCOUNTANT = "ACCOUNTANT";

  public Role(String name, String codeName) {
    this.name = name;
    this.codeName = codeName;
  }

  private String name;

  @Column(name = "code_name")
  private String codeName;

  @JsonManagedReference
  @ManyToMany
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private List<Permission> permissions = new ArrayList<>();

  @JsonBackReference
  @OneToMany(mappedBy = "role")
  private List<Staff> staffs = new ArrayList<>();
}
