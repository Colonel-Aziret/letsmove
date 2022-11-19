package com.letsmove.letsmove.entity;


import com.letsmove.letsmove.enums.PlaceType;
import com.letsmove.letsmove.enums.Status;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "place")
@Getter
@Setter
public class Place{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq")
    @SequenceGenerator(name = "place_id_seq", sequenceName = "place_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User userID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID")
    private City cityID;

    @Column(name = "PLACE_NAME")
    private String placeName;

    @Column(name = "INFO")
    private String info;

    @Column(name = "IMG")
    private String img;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING )
    private Status status;

    @Column(name = "PLACE_TYPE")
    @Enumerated(EnumType.STRING )
    private PlaceType placeType;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

}

