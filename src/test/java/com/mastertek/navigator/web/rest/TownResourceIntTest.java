package com.mastertek.navigator.web.rest;

import com.mastertek.navigator.NavigatorbackendApp;

import com.mastertek.navigator.domain.Town;
import com.mastertek.navigator.repository.TownRepository;
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
 * Test class for the TownResource REST controller.
 *
 * @see TownResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
public class TownResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LNG = "AAAAAAAAAA";
    private static final String UPDATED_LNG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTownMockMvc;

    private Town town;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TownResource townResource = new TownResource(townRepository);
        this.restTownMockMvc = MockMvcBuilders.standaloneSetup(townResource)
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
    public static Town createEntity(EntityManager em) {
        Town town = new Town()
            .name(DEFAULT_NAME)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .completed(DEFAULT_COMPLETED);
        return town;
    }

    @Before
    public void initTest() {
        town = createEntity(em);
    }

    @Test
    @Transactional
    public void createTown() throws Exception {
        int databaseSizeBeforeCreate = townRepository.findAll().size();

        // Create the Town
        restTownMockMvc.perform(post("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(town)))
            .andExpect(status().isCreated());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeCreate + 1);
        Town testTown = townList.get(townList.size() - 1);
        assertThat(testTown.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTown.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testTown.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testTown.isCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createTownWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = townRepository.findAll().size();

        // Create the Town with an existing ID
        town.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTownMockMvc.perform(post("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(town)))
            .andExpect(status().isBadRequest());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = townRepository.findAll().size();
        // set the field null
        town.setName(null);

        // Create the Town, which fails.

        restTownMockMvc.perform(post("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(town)))
            .andExpect(status().isBadRequest());

        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTowns() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);

        // Get all the townList
        restTownMockMvc.perform(get("/api/towns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(town.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.toString())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);

        // Get the town
        restTownMockMvc.perform(get("/api/towns/{id}", town.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(town.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.toString()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTown() throws Exception {
        // Get the town
        restTownMockMvc.perform(get("/api/towns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);
        int databaseSizeBeforeUpdate = townRepository.findAll().size();

        // Update the town
        Town updatedTown = townRepository.findOne(town.getId());
        // Disconnect from session so that the updates on updatedTown are not directly saved in db
        em.detach(updatedTown);
        updatedTown
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .completed(UPDATED_COMPLETED);

        restTownMockMvc.perform(put("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTown)))
            .andExpect(status().isOk());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeUpdate);
        Town testTown = townList.get(townList.size() - 1);
        assertThat(testTown.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTown.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testTown.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testTown.isCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void updateNonExistingTown() throws Exception {
        int databaseSizeBeforeUpdate = townRepository.findAll().size();

        // Create the Town

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTownMockMvc.perform(put("/api/towns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(town)))
            .andExpect(status().isCreated());

        // Validate the Town in the database
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTown() throws Exception {
        // Initialize the database
        townRepository.saveAndFlush(town);
        int databaseSizeBeforeDelete = townRepository.findAll().size();

        // Get the town
        restTownMockMvc.perform(delete("/api/towns/{id}", town.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Town> townList = townRepository.findAll();
        assertThat(townList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Town.class);
        Town town1 = new Town();
        town1.setId(1L);
        Town town2 = new Town();
        town2.setId(town1.getId());
        assertThat(town1).isEqualTo(town2);
        town2.setId(2L);
        assertThat(town1).isNotEqualTo(town2);
        town1.setId(null);
        assertThat(town1).isNotEqualTo(town2);
    }
}
