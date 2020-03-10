package com.mastertek.navigator.web.rest;

import com.mastertek.navigator.NavigatorbackendApp;

import com.mastertek.navigator.domain.InterestPoint;
import com.mastertek.navigator.repository.InterestPointRepository;
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
 * Test class for the InterestPointResource REST controller.
 *
 * @see InterestPointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
public class InterestPointResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LNG = "AAAAAAAAAA";
    private static final String UPDATED_LNG = "BBBBBBBBBB";

    @Autowired
    private InterestPointRepository interestPointRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInterestPointMockMvc;

    private InterestPoint interestPoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterestPointResource interestPointResource = new InterestPointResource(interestPointRepository);
        this.restInterestPointMockMvc = MockMvcBuilders.standaloneSetup(interestPointResource)
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
    public static InterestPoint createEntity(EntityManager em) {
        InterestPoint interestPoint = new InterestPoint()
            .name(DEFAULT_NAME)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG);
        return interestPoint;
    }

    @Before
    public void initTest() {
        interestPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterestPoint() throws Exception {
        int databaseSizeBeforeCreate = interestPointRepository.findAll().size();

        // Create the InterestPoint
        restInterestPointMockMvc.perform(post("/api/interest-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestPoint)))
            .andExpect(status().isCreated());

        // Validate the InterestPoint in the database
        List<InterestPoint> interestPointList = interestPointRepository.findAll();
        assertThat(interestPointList).hasSize(databaseSizeBeforeCreate + 1);
        InterestPoint testInterestPoint = interestPointList.get(interestPointList.size() - 1);
        assertThat(testInterestPoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInterestPoint.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testInterestPoint.getLng()).isEqualTo(DEFAULT_LNG);
    }

    @Test
    @Transactional
    public void createInterestPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interestPointRepository.findAll().size();

        // Create the InterestPoint with an existing ID
        interestPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterestPointMockMvc.perform(post("/api/interest-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestPoint)))
            .andExpect(status().isBadRequest());

        // Validate the InterestPoint in the database
        List<InterestPoint> interestPointList = interestPointRepository.findAll();
        assertThat(interestPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInterestPoints() throws Exception {
        // Initialize the database
        interestPointRepository.saveAndFlush(interestPoint);

        // Get all the interestPointList
        restInterestPointMockMvc.perform(get("/api/interest-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interestPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.toString())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.toString())));
    }

    @Test
    @Transactional
    public void getInterestPoint() throws Exception {
        // Initialize the database
        interestPointRepository.saveAndFlush(interestPoint);

        // Get the interestPoint
        restInterestPointMockMvc.perform(get("/api/interest-points/{id}", interestPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interestPoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.toString()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterestPoint() throws Exception {
        // Get the interestPoint
        restInterestPointMockMvc.perform(get("/api/interest-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterestPoint() throws Exception {
        // Initialize the database
        interestPointRepository.saveAndFlush(interestPoint);
        int databaseSizeBeforeUpdate = interestPointRepository.findAll().size();

        // Update the interestPoint
        InterestPoint updatedInterestPoint = interestPointRepository.findOne(interestPoint.getId());
        // Disconnect from session so that the updates on updatedInterestPoint are not directly saved in db
        em.detach(updatedInterestPoint);
        updatedInterestPoint
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG);

        restInterestPointMockMvc.perform(put("/api/interest-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterestPoint)))
            .andExpect(status().isOk());

        // Validate the InterestPoint in the database
        List<InterestPoint> interestPointList = interestPointRepository.findAll();
        assertThat(interestPointList).hasSize(databaseSizeBeforeUpdate);
        InterestPoint testInterestPoint = interestPointList.get(interestPointList.size() - 1);
        assertThat(testInterestPoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInterestPoint.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testInterestPoint.getLng()).isEqualTo(UPDATED_LNG);
    }

    @Test
    @Transactional
    public void updateNonExistingInterestPoint() throws Exception {
        int databaseSizeBeforeUpdate = interestPointRepository.findAll().size();

        // Create the InterestPoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInterestPointMockMvc.perform(put("/api/interest-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestPoint)))
            .andExpect(status().isCreated());

        // Validate the InterestPoint in the database
        List<InterestPoint> interestPointList = interestPointRepository.findAll();
        assertThat(interestPointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInterestPoint() throws Exception {
        // Initialize the database
        interestPointRepository.saveAndFlush(interestPoint);
        int databaseSizeBeforeDelete = interestPointRepository.findAll().size();

        // Get the interestPoint
        restInterestPointMockMvc.perform(delete("/api/interest-points/{id}", interestPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InterestPoint> interestPointList = interestPointRepository.findAll();
        assertThat(interestPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterestPoint.class);
        InterestPoint interestPoint1 = new InterestPoint();
        interestPoint1.setId(1L);
        InterestPoint interestPoint2 = new InterestPoint();
        interestPoint2.setId(interestPoint1.getId());
        assertThat(interestPoint1).isEqualTo(interestPoint2);
        interestPoint2.setId(2L);
        assertThat(interestPoint1).isNotEqualTo(interestPoint2);
        interestPoint1.setId(null);
        assertThat(interestPoint1).isNotEqualTo(interestPoint2);
    }
}
