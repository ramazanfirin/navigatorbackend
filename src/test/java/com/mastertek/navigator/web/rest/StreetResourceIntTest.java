package com.mastertek.navigator.web.rest;

import com.mastertek.navigator.NavigatorbackendApp;

import com.mastertek.navigator.domain.Street;
import com.mastertek.navigator.repository.StreetRepository;
import com.mastertek.navigator.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mastertek.navigator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StreetResource REST controller.
 *
 * @see StreetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
public class StreetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LNG = "AAAAAAAAAA";
    private static final String UPDATED_LNG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private StreetRepository streetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStreetMockMvc;

    private Street street;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StreetResource streetResource = new StreetResource(streetRepository);
        this.restStreetMockMvc = MockMvcBuilders.standaloneSetup(streetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Street createEntity(EntityManager em) {
        Street street = new Street()
            .name(DEFAULT_NAME)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .completed(DEFAULT_COMPLETED);
        return street;
    }

    @Before
    public void initTest() {
        street = createEntity(em);
    }

    @Test
    @Transactional
    public void createStreet() throws Exception {
        int databaseSizeBeforeCreate = streetRepository.findAll().size();

        // Create the Street
        restStreetMockMvc.perform(post("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(street)))
            .andExpect(status().isCreated());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeCreate + 1);
        Street testStreet = streetList.get(streetList.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStreet.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testStreet.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testStreet.isCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createStreetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = streetRepository.findAll().size();

        // Create the Street with an existing ID
        street.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStreetMockMvc.perform(post("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(street)))
            .andExpect(status().isBadRequest());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = streetRepository.findAll().size();
        // set the field null
        street.setName(null);

        // Create the Street, which fails.

        restStreetMockMvc.perform(post("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(street)))
            .andExpect(status().isBadRequest());

        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStreets() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get all the streetList
        restStreetMockMvc.perform(get("/api/streets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(street.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.toString())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", street.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(street.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.toString()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStreet() throws Exception {
        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);
        int databaseSizeBeforeUpdate = streetRepository.findAll().size();

        // Update the street
        Street updatedStreet = streetRepository.findOne(street.getId());
        // Disconnect from session so that the updates on updatedStreet are not directly saved in db
        em.detach(updatedStreet);
        updatedStreet
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .completed(UPDATED_COMPLETED);

        restStreetMockMvc.perform(put("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStreet)))
            .andExpect(status().isOk());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeUpdate);
        Street testStreet = streetList.get(streetList.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStreet.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testStreet.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testStreet.isCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void updateNonExistingStreet() throws Exception {
        int databaseSizeBeforeUpdate = streetRepository.findAll().size();

        // Create the Street

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStreetMockMvc.perform(put("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(street)))
            .andExpect(status().isCreated());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);
        int databaseSizeBeforeDelete = streetRepository.findAll().size();

        // Get the street
        restStreetMockMvc.perform(delete("/api/streets/{id}", street.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Street.class);
        Street street1 = new Street();
        street1.setId(1L);
        Street street2 = new Street();
        street2.setId(street1.getId());
        assertThat(street1).isEqualTo(street2);
        street2.setId(2L);
        assertThat(street1).isNotEqualTo(street2);
        street1.setId(null);
        assertThat(street1).isNotEqualTo(street2);
    }
}
