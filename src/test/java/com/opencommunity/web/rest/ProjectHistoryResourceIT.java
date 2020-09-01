package com.opencommunity.web.rest;

import com.opencommunity.OpencommunityApp;
import com.opencommunity.config.TestSecurityConfiguration;
import com.opencommunity.domain.ProjectHistory;
import com.opencommunity.repository.ProjectHistoryRepository;
import com.opencommunity.repository.search.ProjectHistorySearchRepository;
import com.opencommunity.service.ProjectHistoryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.opencommunity.domain.enumeration.Language;
/**
 * Integration tests for the {@link ProjectHistoryResource} REST controller.
 */
@SpringBootTest(classes = { OpencommunityApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProjectHistoryResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    @Autowired
    private ProjectHistoryRepository projectHistoryRepository;

    @Autowired
    private ProjectHistoryService projectHistoryService;

    /**
     * This repository is mocked in the com.opencommunity.repository.search test package.
     *
     * @see com.opencommunity.repository.search.ProjectHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectHistorySearchRepository mockProjectHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectHistoryMockMvc;

    private ProjectHistory projectHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectHistory createEntity(EntityManager em) {
        ProjectHistory projectHistory = new ProjectHistory()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .language(DEFAULT_LANGUAGE);
        return projectHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectHistory createUpdatedEntity(EntityManager em) {
        ProjectHistory projectHistory = new ProjectHistory()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .language(UPDATED_LANGUAGE);
        return projectHistory;
    }

    @BeforeEach
    public void initTest() {
        projectHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectHistory() throws Exception {
        int databaseSizeBeforeCreate = projectHistoryRepository.findAll().size();
        // Create the ProjectHistory
        restProjectHistoryMockMvc.perform(post("/api/project-histories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projectHistory)))
            .andExpect(status().isCreated());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectHistory testProjectHistory = projectHistoryList.get(projectHistoryList.size() - 1);
        assertThat(testProjectHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProjectHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProjectHistory.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);

        // Validate the ProjectHistory in Elasticsearch
        verify(mockProjectHistorySearchRepository, times(1)).save(testProjectHistory);
    }

    @Test
    @Transactional
    public void createProjectHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectHistoryRepository.findAll().size();

        // Create the ProjectHistory with an existing ID
        projectHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectHistoryMockMvc.perform(post("/api/project-histories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projectHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProjectHistory in Elasticsearch
        verify(mockProjectHistorySearchRepository, times(0)).save(projectHistory);
    }


    @Test
    @Transactional
    public void getAllProjectHistories() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get all the projectHistoryList
        restProjectHistoryMockMvc.perform(get("/api/project-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }
    
    @Test
    @Transactional
    public void getProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", projectHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectHistory.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProjectHistory() throws Exception {
        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryService.save(projectHistory);

        int databaseSizeBeforeUpdate = projectHistoryRepository.findAll().size();

        // Update the projectHistory
        ProjectHistory updatedProjectHistory = projectHistoryRepository.findById(projectHistory.getId()).get();
        // Disconnect from session so that the updates on updatedProjectHistory are not directly saved in db
        em.detach(updatedProjectHistory);
        updatedProjectHistory
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .language(UPDATED_LANGUAGE);

        restProjectHistoryMockMvc.perform(put("/api/project-histories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectHistory)))
            .andExpect(status().isOk());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProjectHistory testProjectHistory = projectHistoryList.get(projectHistoryList.size() - 1);
        assertThat(testProjectHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProjectHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProjectHistory.getLanguage()).isEqualTo(UPDATED_LANGUAGE);

        // Validate the ProjectHistory in Elasticsearch
        verify(mockProjectHistorySearchRepository, times(2)).save(testProjectHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectHistory() throws Exception {
        int databaseSizeBeforeUpdate = projectHistoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectHistoryMockMvc.perform(put("/api/project-histories").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(projectHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProjectHistory in Elasticsearch
        verify(mockProjectHistorySearchRepository, times(0)).save(projectHistory);
    }

    @Test
    @Transactional
    public void deleteProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryService.save(projectHistory);

        int databaseSizeBeforeDelete = projectHistoryRepository.findAll().size();

        // Delete the projectHistory
        restProjectHistoryMockMvc.perform(delete("/api/project-histories/{id}", projectHistory.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProjectHistory in Elasticsearch
        verify(mockProjectHistorySearchRepository, times(1)).deleteById(projectHistory.getId());
    }

    @Test
    @Transactional
    public void searchProjectHistory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        projectHistoryService.save(projectHistory);
        when(mockProjectHistorySearchRepository.search(queryStringQuery("id:" + projectHistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projectHistory), PageRequest.of(0, 1), 1));

        // Search the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/_search/project-histories?query=id:" + projectHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }
}
