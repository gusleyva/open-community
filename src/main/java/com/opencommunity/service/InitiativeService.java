package com.opencommunity.service;

import com.opencommunity.domain.Initiative;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Initiative}.
 */
public interface InitiativeService {

    /**
     * Save a initiative.
     *
     * @param initiative the entity to save.
     * @return the persisted entity.
     */
    Initiative save(Initiative initiative);

    /**
     * Get all the initiatives.
     *
     * @return the list of entities.
     */
    List<Initiative> findAll();


    /**
     * Get the "id" initiative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Initiative> findOne(Long id);

    /**
     * Delete the "id" initiative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the initiative corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Initiative> search(String query);
}
