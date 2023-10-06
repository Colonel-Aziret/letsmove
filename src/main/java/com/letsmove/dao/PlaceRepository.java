package com.letsmove.dao;

import com.letsmove.entity.Place;
import com.letsmove.entity.Users;
import com.letsmove.enums.PlaceType;
import com.letsmove.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Transactional
    List<Place> findAllByStatus(Status status);

    @Transactional
    Place findPlaceById(Integer id);

    @Transactional
    List<Place> findPlacesByPlaceType(PlaceType placeType);

    @Transactional
    List<Place> findPlacesByStatusAndUsersID(Status status, Users users);

    List<Place> findByCityID(String city);
}
