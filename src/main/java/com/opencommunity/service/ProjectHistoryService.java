package com.opencommunity.service;

import com.opencommunity.domain.ProjectHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ProjectHistory}.
 */
public interface ProjectHistoryService {

    /**
     * Save a projectHistory.
     *
     * @param projectHistory the entity to save.
     * @return the persisted entity.
     */
    ProjectHistory save(ProjectHistory projectHistory);

    /**
     * Get all the projectHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProjectHistory> findAll(Pageable pageable);


    /**
     * Get the "id" projectHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProjectHistory> findOne(Long id);

    /**
     * Delete the "id" projectHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the projectHistory corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProjectHistory> search(String query, Pageable pageable);
}
