package com.mastertek.navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mastertek.navigator.domain.Building;


/**
 * Spring Data JPA repository for the Building entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

	@Query("select item from Building item where item.lat=:lat and item.lng=:lng and item.street.id=:streetId")
    Building findBuilding(@Param("lat")String lat,@Param("lng")String lng,@Param("streetId")Long streetId);

	@Query("select item from Building item where item.name=:name and item.number=:number and item.street.id=:streetId")
    Building findBuildingByName(@Param("name")String name,@Param("number")String number,@Param("streetId")Long streetId);
	
	@Query("select item from Building item where item.street.id=:streetId")
    List<Building> findBuildingByStreetId(@Param("streetId")Long streetId);
	
}
