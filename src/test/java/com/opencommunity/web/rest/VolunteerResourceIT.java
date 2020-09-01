package com.opencommunity.web.rest;

import com.opencommunity.OpencommunityApp;
import com.opencommunity.config.TestSecurityConfiguration;
import com.opencommunity.domain.Volunteer;
import com.opencommunity.repository.VolunteerRepository;
import com.opencommunity.repository.search.VolunteerSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VolunteerResource} REST controller.
 */
@SpringBootTest(classes = { OpencommunityApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class VolunteerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Autowired
    private VolunteerRepository volunteerRepository;

    /**
     * This repository is mocked in the com.opencommunity.repository.search test package.
     *
     * @see com.opencommunity.repository.search.VolunteerSearchRepositoryMockConfiguration
     */
    @Autowired
    private VolunteerSearchRepository mockVolunteerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVolunteerMockMvc;

    private Volunteer volunteer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Volunteer createEntity(EntityManager em) {
        Volunteer volunteer = new Volunteer()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .age(DEFAULT_AGE);
        return volunteer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Volunteer createUpdatedEntity(EntityManager em) {
        Volunteer volunteer = new Volunteer()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .age(UPDATED_AGE);
        return volunteer;
    }

    @BeforeEach
    public void initTest() {
        volunteer = createEntity(em);
    }

    @Test
    @Transactional
    public void createVolunteer() throws Exception {
        int databaseSizeBeforeCreate = volunteerRepository.findAll().size();
        // Create the Volunteer
        restVolunteerMockMvc.perform(post("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isCreated());

        // Validate the Volunteer in the database
        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeCreate + 1);
        Volunteer testVolunteer = volunteerList.get(volunteerList.size() - 1);
        assertThat(testVolunteer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVolunteer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testVolunteer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVolunteer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testVolunteer.getAge()).isEqualTo(DEFAULT_AGE);

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(1)).save(testVolunteer);
    }

    @Test
    @Transactional
    public void createVolunteerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = volunteerRepository.findAll().size();

        // Create the Volunteer with an existing ID
        volunteer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVolunteerMockMvc.perform(post("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isBadRequest());

        // Validate the Volunteer in the database
        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(0)).save(volunteer);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = volunteerRepository.findAll().size();
        // set the field null
        volunteer.setFirstName(null);

        // Create the Volunteer, which fails.


        restVolunteerMockMvc.perform(post("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isBadRequest());

        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = volunteerRepository.findAll().size();
        // set the field null
        volunteer.setLastName(null);

        // Create the Volunteer, which fails.


        restVolunteerMockMvc.perform(post("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isBadRequest());

        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = volunteerRepository.findAll().size();
        // set the field null
        volunteer.setEmail(null);

        // Create the Volunteer, which fails.


        restVolunteerMockMvc.perform(post("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isBadRequest());

        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVolunteers() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        // Get all the volunteerList
        restVolunteerMockMvc.perform(get("/api/volunteers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
    
    @Test
    @Transactional
    public void getVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", volunteer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(volunteer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }
    @Test
    @Transactional
    public void getNonExistingVolunteer() throws Exception {
        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        int databaseSizeBeforeUpdate = volunteerRepository.findAll().size();

        // Update the volunteer
        Volunteer updatedVolunteer = volunteerRepository.findById(volunteer.getId()).get();
        // Disconnect from session so that the updates on updatedVolunteer are not directly saved in db
        em.detach(updatedVolunteer);
        updatedVolunteer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .age(UPDATED_AGE);

        restVolunteerMockMvc.perform(put("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVolunteer)))
            .andExpect(status().isOk());

        // Validate the Volunteer in the database
        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeUpdate);
        Volunteer testVolunteer = volunteerList.get(volunteerList.size() - 1);
        assertThat(testVolunteer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVolunteer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testVolunteer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVolunteer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testVolunteer.getAge()).isEqualTo(UPDATED_AGE);

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(1)).save(testVolunteer);
    }

    @Test
    @Transactional
    public void updateNonExistingVolunteer() throws Exception {
        int databaseSizeBeforeUpdate = volunteerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVolunteerMockMvc.perform(put("/api/volunteers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(volunteer)))
            .andExpect(status().isBadRequest());

        // Validate the Volunteer in the database
        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(0)).save(volunteer);
    }

    @Test
    @Transactional
    public void deleteVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        int databaseSizeBeforeDelete = volunteerRepository.findAll().size();

        // Delete the volunteer
        restVolunteerMockMvc.perform(delete("/api/volunteers/{id}", volunteer.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Volunteer> volunteerList = volunteerRepository.findAll();
        assertThat(volunteerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(1)).deleteById(volunteer.getId());
    }

    @Test
    @Transactional
    public void searchVolunteer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);
        when(mockVolunteerSearchRepository.search(queryStringQuery("id:" + volunteer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(volunteer), PageRequest.of(0, 1), 1));

        // Search the volunteer
        restVolunteerMockMvc.perform(get("/api/_search/volunteers?query=id:" + volunteer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
}
