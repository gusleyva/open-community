package com.opencommunity.web.rest;

import com.opencommunity.OpencommunityApp;
import com.opencommunity.config.TestSecurityConfiguration;
import com.opencommunity.domain.Initiative;
import com.opencommunity.repository.InitiativeRepository;
import com.opencommunity.repository.search.InitiativeSearchRepository;
import com.opencommunity.service.InitiativeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@link InitiativeResource} REST controller.
 */
@SpringBootTest(classes = { OpencommunityApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InitiativeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private InitiativeRepository initiativeRepository;

    @Autowired
    private InitiativeService initiativeService;

    /**
     * This repository is mocked in the com.opencommunity.repository.search test package.
     *
     * @see com.opencommunity.repository.search.InitiativeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InitiativeSearchRepository mockInitiativeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInitiativeMockMvc;

    private Initiative initiative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Initiative createEntity(EntityManager em) {
        Initiative initiative = new Initiative()
            .title(DEFAULT_TITLE);
        return initiative;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Initiative createUpdatedEntity(EntityManager em) {
        Initiative initiative = new Initiative()
            .title(UPDATED_TITLE);
        return initiative;
    }

    @BeforeEach
    public void initTest() {
        initiative = createEntity(em);
    }

    @Test
    @Transactional
    public void createInitiative() throws Exception {
        int databaseSizeBeforeCreate = initiativeRepository.findAll().size();
        // Create the Initiative
        restInitiativeMockMvc.perform(post("/api/initiatives").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(initiative)))
            .andExpect(status().isCreated());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeCreate + 1);
        Initiative testInitiative = initiativeList.get(initiativeList.size() - 1);
        assertThat(testInitiative.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the Initiative in Elasticsearch
        verify(mockInitiativeSearchRepository, times(1)).save(testInitiative);
    }

    @Test
    @Transactional
    public void createInitiativeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = initiativeRepository.findAll().size();

        // Create the Initiative with an existing ID
        initiative.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInitiativeMockMvc.perform(post("/api/initiatives").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(initiative)))
            .andExpect(status().isBadRequest());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Initiative in Elasticsearch
        verify(mockInitiativeSearchRepository, times(0)).save(initiative);
    }


    @Test
    @Transactional
    public void getAllInitiatives() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);

        // Get all the initiativeList
        restInitiativeMockMvc.perform(get("/api/initiatives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initiative.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
    
    @Test
    @Transactional
    public void getInitiative() throws Exception {
        // Initialize the database
        initiativeRepository.saveAndFlush(initiative);

        // Get the initiative
        restInitiativeMockMvc.perform(get("/api/initiatives/{id}", initiative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(initiative.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }
    @Test
    @Transactional
    public void getNonExistingInitiative() throws Exception {
        // Get the initiative
        restInitiativeMockMvc.perform(get("/api/initiatives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInitiative() throws Exception {
        // Initialize the database
        initiativeService.save(initiative);

        int databaseSizeBeforeUpdate = initiativeRepository.findAll().size();

        // Update the initiative
        Initiative updatedInitiative = initiativeRepository.findById(initiative.getId()).get();
        // Disconnect from session so that the updates on updatedInitiative are not directly saved in db
        em.detach(updatedInitiative);
        updatedInitiative
            .title(UPDATED_TITLE);

        restInitiativeMockMvc.perform(put("/api/initiatives").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInitiative)))
            .andExpect(status().isOk());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeUpdate);
        Initiative testInitiative = initiativeList.get(initiativeList.size() - 1);
        assertThat(testInitiative.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the Initiative in Elasticsearch
        verify(mockInitiativeSearchRepository, times(2)).save(testInitiative);
    }

    @Test
    @Transactional
    public void updateNonExistingInitiative() throws Exception {
        int databaseSizeBeforeUpdate = initiativeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInitiativeMockMvc.perform(put("/api/initiatives").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(initiative)))
            .andExpect(status().isBadRequest());

        // Validate the Initiative in the database
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Initiative in Elasticsearch
        verify(mockInitiativeSearchRepository, times(0)).save(initiative);
    }

    @Test
    @Transactional
    public void deleteInitiative() throws Exception {
        // Initialize the database
        initiativeService.save(initiative);

        int databaseSizeBeforeDelete = initiativeRepository.findAll().size();

        // Delete the initiative
        restInitiativeMockMvc.perform(delete("/api/initiatives/{id}", initiative.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Initiative> initiativeList = initiativeRepository.findAll();
        assertThat(initiativeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Initiative in Elasticsearch
        verify(mockInitiativeSearchRepository, times(1)).deleteById(initiative.getId());
    }

    @Test
    @Transactional
    public void searchInitiative() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        initiativeService.save(initiative);
        when(mockInitiativeSearchRepository.search(queryStringQuery("id:" + initiative.getId())))
            .thenReturn(Collections.singletonList(initiative));

        // Search the initiative
        restInitiativeMockMvc.perform(get("/api/_search/initiatives?query=id:" + initiative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initiative.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
}
