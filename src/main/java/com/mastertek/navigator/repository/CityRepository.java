package com.mastertek.navigator.repository;

import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.domain.District;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	City findOneByName(String name);

}
