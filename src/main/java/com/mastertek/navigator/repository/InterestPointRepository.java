package com.mastertek.navigator.repository;

import com.mastertek.navigator.domain.InterestPoint;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InterestPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterestPointRepository extends JpaRepository<InterestPoint, Long> {

}
