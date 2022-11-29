package com.letsmove.dao;

import com.letsmove.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
    @Override
    List<City> findAll();
    City findCityByName(String name);
}
