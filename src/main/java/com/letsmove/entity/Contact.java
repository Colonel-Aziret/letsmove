package com.letsmove.entity;

import com.letsmove.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;



    @Column(name = "comment")
    private String comment;



    @Column(name = "EMAIL")
    private String email;


    @Column(name = "name")
    private String name;


}
