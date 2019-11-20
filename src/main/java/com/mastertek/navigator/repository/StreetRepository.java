package com.mastertek.navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mastertek.navigator.domain.Street;


/**
 * Spring Data JPA repository for the Street entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {

	@Query("select item from Street item where item.name=:name and item.town.id=:townId")
    Street findStreetByName(@Param("name")String name,@Param("townId")Long townId);
	
	@Query("select item from Street item where  item.town.id=:townId")
    List<Street> findStreetByTownId(@Param("townId")Long townId);

}
