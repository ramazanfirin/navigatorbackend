package com.mastertek.navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mastertek.navigator.domain.District;


/**
 * Spring Data JPA repository for the District entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

	@Query("select district from District district where district.name=:name and district.city.id=:cityId")
    District findDistrictByName(@Param("name")String name,@Param("cityId")Long cityId);

	@Query("select district from District district where district.city.id=:cityId")
    List<District> findByDistrictCityId(@Param("cityId")Long cityId);

}
