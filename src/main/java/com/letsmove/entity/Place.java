package com.letsmove.entity;


import com.letsmove.enums.PlaceType;
import com.letsmove.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "place")
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq")
    @SequenceGenerator(name = "place_id_seq", sequenceName = "place_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private Users usersID;


    @Column(name = "CITY")
    private String cityID;

    @Column(name = "PLACE_NAME")
    private String placeName;

    @Lob
    @Column(name = "INFO")
    private String info;

    @Lob
    @Column(name = "IMG")
    private String img;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "PLACE_TYPE")
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

}

