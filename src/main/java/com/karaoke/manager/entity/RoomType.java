package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room_types")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class RoomType extends BaseEntity{

    private String name;

    @Column(name = "code_name")
    private String codeName;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms = new ArrayList<>();
}
