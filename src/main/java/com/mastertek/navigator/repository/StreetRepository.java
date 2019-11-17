package com.mastertek.navigator.repository;

import com.mastertek.navigator.domain.Street;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Street entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {

}
