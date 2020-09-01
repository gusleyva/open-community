package com.opencommunity.web.rest;

import com.opencommunity.domain.ProjectHistory;
import com.opencommunity.service.ProjectHistoryService;
import com.opencommunity.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.opencommunity.domain.ProjectHistory}.
 */
@RestController
@RequestMapping("/api")
public class ProjectHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProjectHistoryResource.class);

    private static final String ENTITY_NAME = "projectHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectHistoryService projectHistoryService;

    public ProjectHistoryResource(ProjectHistoryService projectHistoryService) {
        this.projectHistoryService = projectHistoryService;
    }

    /**
     * {@code POST  /project-histories} : Create a new projectHistory.
     *
     * @param projectHistory the projectHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectHistory, or with status {@code 400 (Bad Request)} if the projectHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-histories")
    public ResponseEntity<ProjectHistory> createProjectHistory(@RequestBody ProjectHistory projectHistory) throws URISyntaxException {
        log.debug("REST request to save ProjectHistory : {}", projectHistory);
        if (projectHistory.getId() != null) {
            throw new BadRequestAlertException("A new projectHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectHistory result = projectHistoryService.save(projectHistory);
        return ResponseEntity.created(new URI("/api/project-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-histories} : Updates an existing projectHistory.
     *
     * @param projectHistory the projectHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectHistory,
     * or with status {@code 400 (Bad Request)} if the projectHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-histories")
    public ResponseEntity<ProjectHistory> updateProjectHistory(@RequestBody ProjectHistory projectHistory) throws URISyntaxException {
        log.debug("REST request to update ProjectHistory : {}", projectHistory);
        if (projectHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectHistory result = projectHistoryService.save(projectHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /project-histories} : get all the projectHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectHistories in body.
     */
    @GetMapping("/project-histories")
    public ResponseEntity<List<ProjectHistory>> getAllProjectHistories(Pageable pageable) {
        log.debug("REST request to get a page of ProjectHistories");
        Page<ProjectHistory> page = projectHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-histories/:id} : get the "id" projectHistory.
     *
     * @param id the id of the projectHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-histories/{id}")
    public ResponseEntity<ProjectHistory> getProjectHistory(@PathVariable Long id) {
        log.debug("REST request to get ProjectHistory : {}", id);
        Optional<ProjectHistory> projectHistory = projectHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectHistory);
    }

    /**
     * {@code DELETE  /project-histories/:id} : delete the "id" projectHistory.
     *
     * @param id the id of the projectHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-histories/{id}")
    public ResponseEntity<Void> deleteProjectHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProjectHistory : {}", id);
        projectHistoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/project-histories?query=:query} : search for the projectHistory corresponding
     * to the query.
     *
     * @param query the query of the projectHistory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/project-histories")
    public ResponseEntity<List<ProjectHistory>> searchProjectHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectHistories for query {}", query);
        Page<ProjectHistory> page = projectHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
