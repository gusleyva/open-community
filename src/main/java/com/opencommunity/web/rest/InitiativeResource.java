package com.opencommunity.web.rest;

import com.opencommunity.domain.Initiative;
import com.opencommunity.service.InitiativeService;
import com.opencommunity.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.opencommunity.domain.Initiative}.
 */
@RestController
@RequestMapping("/api")
public class InitiativeResource {

    private final Logger log = LoggerFactory.getLogger(InitiativeResource.class);

    private static final String ENTITY_NAME = "initiative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InitiativeService initiativeService;

    public InitiativeResource(InitiativeService initiativeService) {
        this.initiativeService = initiativeService;
    }

    /**
     * {@code POST  /initiatives} : Create a new initiative.
     *
     * @param initiative the initiative to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new initiative, or with status {@code 400 (Bad Request)} if the initiative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/initiatives")
    public ResponseEntity<Initiative> createInitiative(@RequestBody Initiative initiative) throws URISyntaxException {
        log.debug("REST request to save Initiative : {}", initiative);
        if (initiative.getId() != null) {
            throw new BadRequestAlertException("A new initiative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Initiative result = initiativeService.save(initiative);
        return ResponseEntity.created(new URI("/api/initiatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /initiatives} : Updates an existing initiative.
     *
     * @param initiative the initiative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated initiative,
     * or with status {@code 400 (Bad Request)} if the initiative is not valid,
     * or with status {@code 500 (Internal Server Error)} if the initiative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/initiatives")
    public ResponseEntity<Initiative> updateInitiative(@RequestBody Initiative initiative) throws URISyntaxException {
        log.debug("REST request to update Initiative : {}", initiative);
        if (initiative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Initiative result = initiativeService.save(initiative);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, initiative.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /initiatives} : get all the initiatives.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of initiatives in body.
     */
    @GetMapping("/initiatives")
    public List<Initiative> getAllInitiatives() {
        log.debug("REST request to get all Initiatives");
        return initiativeService.findAll();
    }

    /**
     * {@code GET  /initiatives/:id} : get the "id" initiative.
     *
     * @param id the id of the initiative to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the initiative, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/initiatives/{id}")
    public ResponseEntity<Initiative> getInitiative(@PathVariable Long id) {
        log.debug("REST request to get Initiative : {}", id);
        Optional<Initiative> initiative = initiativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(initiative);
    }

    /**
     * {@code DELETE  /initiatives/:id} : delete the "id" initiative.
     *
     * @param id the id of the initiative to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/initiatives/{id}")
    public ResponseEntity<Void> deleteInitiative(@PathVariable Long id) {
        log.debug("REST request to delete Initiative : {}", id);
        initiativeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/initiatives?query=:query} : search for the initiative corresponding
     * to the query.
     *
     * @param query the query of the initiative search.
     * @return the result of the search.
     */
    @GetMapping("/_search/initiatives")
    public List<Initiative> searchInitiatives(@RequestParam String query) {
        log.debug("REST request to search Initiatives for query {}", query);
        return initiativeService.search(query);
    }
}
