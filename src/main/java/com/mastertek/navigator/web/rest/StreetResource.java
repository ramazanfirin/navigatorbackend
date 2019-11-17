package com.mastertek.navigator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.navigator.domain.Street;

import com.mastertek.navigator.repository.StreetRepository;
import com.mastertek.navigator.web.rest.errors.BadRequestAlertException;
import com.mastertek.navigator.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Street.
 */
@RestController
@RequestMapping("/api")
public class StreetResource {

    private final Logger log = LoggerFactory.getLogger(StreetResource.class);

    private static final String ENTITY_NAME = "street";

    private final StreetRepository streetRepository;

    public StreetResource(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    /**
     * POST  /streets : Create a new street.
     *
     * @param street the street to create
     * @return the ResponseEntity with status 201 (Created) and with body the new street, or with status 400 (Bad Request) if the street has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/streets")
    @Timed
    public ResponseEntity<Street> createStreet(@Valid @RequestBody Street street) throws URISyntaxException {
        log.debug("REST request to save Street : {}", street);
        if (street.getId() != null) {
            throw new BadRequestAlertException("A new street cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Street result = streetRepository.save(street);
        return ResponseEntity.created(new URI("/api/streets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /streets : Updates an existing street.
     *
     * @param street the street to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated street,
     * or with status 400 (Bad Request) if the street is not valid,
     * or with status 500 (Internal Server Error) if the street couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/streets")
    @Timed
    public ResponseEntity<Street> updateStreet(@Valid @RequestBody Street street) throws URISyntaxException {
        log.debug("REST request to update Street : {}", street);
        if (street.getId() == null) {
            return createStreet(street);
        }
        Street result = streetRepository.save(street);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, street.getId().toString()))
            .body(result);
    }

    /**
     * GET  /streets : get all the streets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of streets in body
     */
    @GetMapping("/streets")
    @Timed
    public List<Street> getAllStreets() {
        log.debug("REST request to get all Streets");
        return streetRepository.findAll();
        }

    /**
     * GET  /streets/:id : get the "id" street.
     *
     * @param id the id of the street to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the street, or with status 404 (Not Found)
     */
    @GetMapping("/streets/{id}")
    @Timed
    public ResponseEntity<Street> getStreet(@PathVariable Long id) {
        log.debug("REST request to get Street : {}", id);
        Street street = streetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(street));
    }

    /**
     * DELETE  /streets/:id : delete the "id" street.
     *
     * @param id the id of the street to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/streets/{id}")
    @Timed
    public ResponseEntity<Void> deleteStreet(@PathVariable Long id) {
        log.debug("REST request to delete Street : {}", id);
        streetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
