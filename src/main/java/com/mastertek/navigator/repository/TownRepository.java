package com.mastertek.navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mastertek.navigator.domain.Town;


/**
 * Spring Data JPA repository for the Town entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

	@Query("select item from Town item where item.name=:name and item.district.id=:district")
    Town findTownByName(@Param("name")String name,@Param("district")Long district);

	@Query("select item from Town item where item.district.id=:district")
    List<Town> findTownByDistrictId(@Param("district")Long district);

}
