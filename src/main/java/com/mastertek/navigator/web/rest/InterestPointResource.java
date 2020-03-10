package com.mastertek.navigator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.navigator.domain.InterestPoint;

import com.mastertek.navigator.repository.InterestPointRepository;
import com.mastertek.navigator.web.rest.errors.BadRequestAlertException;
import com.mastertek.navigator.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InterestPoint.
 */
@RestController
@RequestMapping("/api")
public class InterestPointResource {

    private final Logger log = LoggerFactory.getLogger(InterestPointResource.class);

    private static final String ENTITY_NAME = "interestPoint";

    private final InterestPointRepository interestPointRepository;

    public InterestPointResource(InterestPointRepository interestPointRepository) {
        this.interestPointRepository = interestPointRepository;
    }

    /**
     * POST  /interest-points : Create a new interestPoint.
     *
     * @param interestPoint the interestPoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interestPoint, or with status 400 (Bad Request) if the interestPoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interest-points")
    @Timed
    public ResponseEntity<InterestPoint> createInterestPoint(@RequestBody InterestPoint interestPoint) throws URISyntaxException {
        log.debug("REST request to save InterestPoint : {}", interestPoint);
        if (interestPoint.getId() != null) {
            throw new BadRequestAlertException("A new interestPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterestPoint result = interestPointRepository.save(interestPoint);
        return ResponseEntity.created(new URI("/api/interest-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interest-points : Updates an existing interestPoint.
     *
     * @param interestPoint the interestPoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interestPoint,
     * or with status 400 (Bad Request) if the interestPoint is not valid,
     * or with status 500 (Internal Server Error) if the interestPoint couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interest-points")
    @Timed
    public ResponseEntity<InterestPoint> updateInterestPoint(@RequestBody InterestPoint interestPoint) throws URISyntaxException {
        log.debug("REST request to update InterestPoint : {}", interestPoint);
        if (interestPoint.getId() == null) {
            return createInterestPoint(interestPoint);
        }
        InterestPoint result = interestPointRepository.save(interestPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interestPoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interest-points : get all the interestPoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interestPoints in body
     */
    @GetMapping("/interest-points")
    @Timed
    public List<InterestPoint> getAllInterestPoints() {
        log.debug("REST request to get all InterestPoints");
        return interestPointRepository.findAll();
        }

    /**
     * GET  /interest-points/:id : get the "id" interestPoint.
     *
     * @param id the id of the interestPoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interestPoint, or with status 404 (Not Found)
     */
    @GetMapping("/interest-points/{id}")
    @Timed
    public ResponseEntity<InterestPoint> getInterestPoint(@PathVariable Long id) {
        log.debug("REST request to get InterestPoint : {}", id);
        InterestPoint interestPoint = interestPointRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(interestPoint));
    }

    /**
     * DELETE  /interest-points/:id : delete the "id" interestPoint.
     *
     * @param id the id of the interestPoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interest-points/{id}")
    @Timed
    public ResponseEntity<Void> deleteInterestPoint(@PathVariable Long id) {
        log.debug("REST request to delete InterestPoint : {}", id);
        interestPointRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
