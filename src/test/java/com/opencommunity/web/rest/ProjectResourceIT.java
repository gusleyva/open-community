package com.opencommunity.web.rest;

import com.opencommunity.OpencommunityApp;
import com.opencommunity.config.TestSecurityConfiguration;
import com.opencommunity.domain.Project;
import com.opencommunity.repository.ProjectRepository;
import com.opencommunity.repository.search.ProjectSearchRepository;

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
import java.util.ArrayList;
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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@SpringBootTest(classes = { OpencommunityApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProjectResourceIT {

    private static final String DEFAULT_PROJECT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REGISTRATION_DEAD_LINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTRATION_DEAD_LINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VOLUNTEER_TASK = "AAAAAAAAAA";
    private static final String UPDATED_VOLUNTEER_TASK = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_VOLUNTEER = 1;
    private static final Integer UPDATED_MAX_VOLUNTEER = 2;

    private static final Integer DEFAULT_MIN_AGE = 1;
    private static final Integer UPDATED_MIN_AGE = 2;

    private static final Integer DEFAULT_MAX_GUEST = 1;
    private static final Integer UPDATED_MAX_GUEST = 2;

    private static final String DEFAULT_VOLUNTEER_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_VOLUNTEER_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_PROJECT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_PROJECT_DETAILS = "BBBBBBBBBB";

    @Autowired
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepository projectRepositoryMock;

    /**
     * This repository is mocked in the com.opencommunity.repository.search test package.
     *
     * @see com.opencommunity.repository.search.ProjectSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectSearchRepository mockProjectSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectTitle(DEFAULT_PROJECT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .registrationDeadLine(DEFAULT_REGISTRATION_DEAD_LINE)
            .description(DEFAULT_DESCRIPTION)
            .volunteerTask(DEFAULT_VOLUNTEER_TASK)
            .maxVolunteer(DEFAULT_MAX_VOLUNTEER)
            .minAge(DEFAULT_MIN_AGE)
            .maxGuest(DEFAULT_MAX_GUEST)
            .volunteerInstructions(DEFAULT_VOLUNTEER_INSTRUCTIONS)
            .additionalProjectDetails(DEFAULT_ADDITIONAL_PROJECT_DETAILS);
        return project;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .projectTitle(UPDATED_PROJECT_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .registrationDeadLine(UPDATED_REGISTRATION_DEAD_LINE)
            .description(UPDATED_DESCRIPTION)
            .volunteerTask(UPDATED_VOLUNTEER_TASK)
            .maxVolunteer(UPDATED_MAX_VOLUNTEER)
            .minAge(UPDATED_MIN_AGE)
            .maxGuest(UPDATED_MAX_GUEST)
            .volunteerInstructions(UPDATED_VOLUNTEER_INSTRUCTIONS)
            .additionalProjectDetails(UPDATED_ADDITIONAL_PROJECT_DETAILS);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        restProjectMockMvc.perform(post("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectTitle()).isEqualTo(DEFAULT_PROJECT_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProject.getRegistrationDeadLine()).isEqualTo(DEFAULT_REGISTRATION_DEAD_LINE);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getVolunteerTask()).isEqualTo(DEFAULT_VOLUNTEER_TASK);
        assertThat(testProject.getMaxVolunteer()).isEqualTo(DEFAULT_MAX_VOLUNTEER);
        assertThat(testProject.getMinAge()).isEqualTo(DEFAULT_MIN_AGE);
        assertThat(testProject.getMaxGuest()).isEqualTo(DEFAULT_MAX_GUEST);
        assertThat(testProject.getVolunteerInstructions()).isEqualTo(DEFAULT_VOLUNTEER_INSTRUCTIONS);
        assertThat(testProject.getAdditionalProjectDetails()).isEqualTo(DEFAULT_ADDITIONAL_PROJECT_DETAILS);

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(1)).save(testProject);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(0)).save(project);
    }


    @Test
    @Transactional
    public void checkProjectTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setProjectTitle(null);

        // Create the Project, which fails.


        restProjectMockMvc.perform(post("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setStartDate(null);

        // Create the Project, which fails.


        restProjectMockMvc.perform(post("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistrationDeadLineIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setRegistrationDeadLine(null);

        // Create the Project, which fails.


        restProjectMockMvc.perform(post("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].registrationDeadLine").value(hasItem(DEFAULT_REGISTRATION_DEAD_LINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].volunteerTask").value(hasItem(DEFAULT_VOLUNTEER_TASK)))
            .andExpect(jsonPath("$.[*].maxVolunteer").value(hasItem(DEFAULT_MAX_VOLUNTEER)))
            .andExpect(jsonPath("$.[*].minAge").value(hasItem(DEFAULT_MIN_AGE)))
            .andExpect(jsonPath("$.[*].maxGuest").value(hasItem(DEFAULT_MAX_GUEST)))
            .andExpect(jsonPath("$.[*].volunteerInstructions").value(hasItem(DEFAULT_VOLUNTEER_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].additionalProjectDetails").value(hasItem(DEFAULT_ADDITIONAL_PROJECT_DETAILS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
            .andExpect(status().isOk());

        verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
            .andExpect(status().isOk());

        verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectTitle").value(DEFAULT_PROJECT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.registrationDeadLine").value(DEFAULT_REGISTRATION_DEAD_LINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.volunteerTask").value(DEFAULT_VOLUNTEER_TASK))
            .andExpect(jsonPath("$.maxVolunteer").value(DEFAULT_MAX_VOLUNTEER))
            .andExpect(jsonPath("$.minAge").value(DEFAULT_MIN_AGE))
            .andExpect(jsonPath("$.maxGuest").value(DEFAULT_MAX_GUEST))
            .andExpect(jsonPath("$.volunteerInstructions").value(DEFAULT_VOLUNTEER_INSTRUCTIONS))
            .andExpect(jsonPath("$.additionalProjectDetails").value(DEFAULT_ADDITIONAL_PROJECT_DETAILS));
    }
    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .projectTitle(UPDATED_PROJECT_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .registrationDeadLine(UPDATED_REGISTRATION_DEAD_LINE)
            .description(UPDATED_DESCRIPTION)
            .volunteerTask(UPDATED_VOLUNTEER_TASK)
            .maxVolunteer(UPDATED_MAX_VOLUNTEER)
            .minAge(UPDATED_MIN_AGE)
            .maxGuest(UPDATED_MAX_GUEST)
            .volunteerInstructions(UPDATED_VOLUNTEER_INSTRUCTIONS)
            .additionalProjectDetails(UPDATED_ADDITIONAL_PROJECT_DETAILS);

        restProjectMockMvc.perform(put("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectTitle()).isEqualTo(UPDATED_PROJECT_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProject.getRegistrationDeadLine()).isEqualTo(UPDATED_REGISTRATION_DEAD_LINE);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getVolunteerTask()).isEqualTo(UPDATED_VOLUNTEER_TASK);
        assertThat(testProject.getMaxVolunteer()).isEqualTo(UPDATED_MAX_VOLUNTEER);
        assertThat(testProject.getMinAge()).isEqualTo(UPDATED_MIN_AGE);
        assertThat(testProject.getMaxGuest()).isEqualTo(UPDATED_MAX_GUEST);
        assertThat(testProject.getVolunteerInstructions()).isEqualTo(UPDATED_VOLUNTEER_INSTRUCTIONS);
        assertThat(testProject.getAdditionalProjectDetails()).isEqualTo(UPDATED_ADDITIONAL_PROJECT_DETAILS);

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(1)).save(testProject);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(put("/api/projects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(0)).save(project);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(1)).deleteById(project.getId());
    }

    @Test
    @Transactional
    public void searchProject() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        projectRepository.saveAndFlush(project);
        when(mockProjectSearchRepository.search(queryStringQuery("id:" + project.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(project), PageRequest.of(0, 1), 1));

        // Search the project
        restProjectMockMvc.perform(get("/api/_search/projects?query=id:" + project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].registrationDeadLine").value(hasItem(DEFAULT_REGISTRATION_DEAD_LINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].volunteerTask").value(hasItem(DEFAULT_VOLUNTEER_TASK)))
            .andExpect(jsonPath("$.[*].maxVolunteer").value(hasItem(DEFAULT_MAX_VOLUNTEER)))
            .andExpect(jsonPath("$.[*].minAge").value(hasItem(DEFAULT_MIN_AGE)))
            .andExpect(jsonPath("$.[*].maxGuest").value(hasItem(DEFAULT_MAX_GUEST)))
            .andExpect(jsonPath("$.[*].volunteerInstructions").value(hasItem(DEFAULT_VOLUNTEER_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].additionalProjectDetails").value(hasItem(DEFAULT_ADDITIONAL_PROJECT_DETAILS)));
    }
}
