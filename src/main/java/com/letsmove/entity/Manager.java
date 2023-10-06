package com.letsmove.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "manager.html")
@Getter
@Setter
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_id_seq")
    @SequenceGenerator(name = "manager_id_seq", sequenceName = "manager_id_seq", allocationSize = 1)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users usersID;

    @Column(name = "FIO")
    private String fio;

    @Column(name = "NUMBER_PHONE")
    private String phoneNumber;

    @Column(name = "ALL_PLACES")
    private Integer allPlaces;
}
