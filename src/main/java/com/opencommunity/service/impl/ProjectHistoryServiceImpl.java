package com.opencommunity.service.impl;

import com.opencommunity.service.ProjectHistoryService;
import com.opencommunity.domain.ProjectHistory;
import com.opencommunity.repository.ProjectHistoryRepository;
import com.opencommunity.repository.search.ProjectHistorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ProjectHistory}.
 */
@Service
@Transactional
public class ProjectHistoryServiceImpl implements ProjectHistoryService {

    private final Logger log = LoggerFactory.getLogger(ProjectHistoryServiceImpl.class);

    private final ProjectHistoryRepository projectHistoryRepository;

    private final ProjectHistorySearchRepository projectHistorySearchRepository;

    public ProjectHistoryServiceImpl(ProjectHistoryRepository projectHistoryRepository, ProjectHistorySearchRepository projectHistorySearchRepository) {
        this.projectHistoryRepository = projectHistoryRepository;
        this.projectHistorySearchRepository = projectHistorySearchRepository;
    }

    @Override
    public ProjectHistory save(ProjectHistory projectHistory) {
        log.debug("Request to save ProjectHistory : {}", projectHistory);
        ProjectHistory result = projectHistoryRepository.save(projectHistory);
        projectHistorySearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectHistory> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectHistories");
        return projectHistoryRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectHistory> findOne(Long id) {
        log.debug("Request to get ProjectHistory : {}", id);
        return projectHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectHistory : {}", id);
        projectHistoryRepository.deleteById(id);
        projectHistorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectHistory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectHistories for query {}", query);
        return projectHistorySearchRepository.search(queryStringQuery(query), pageable);    }
}
