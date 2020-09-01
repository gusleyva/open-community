package com.opencommunity.service.impl;

import com.opencommunity.service.InitiativeService;
import com.opencommunity.domain.Initiative;
import com.opencommunity.repository.InitiativeRepository;
import com.opencommunity.repository.search.InitiativeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Initiative}.
 */
@Service
@Transactional
public class InitiativeServiceImpl implements InitiativeService {

    private final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class);

    private final InitiativeRepository initiativeRepository;

    private final InitiativeSearchRepository initiativeSearchRepository;

    public InitiativeServiceImpl(InitiativeRepository initiativeRepository, InitiativeSearchRepository initiativeSearchRepository) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeSearchRepository = initiativeSearchRepository;
    }

    @Override
    public Initiative save(Initiative initiative) {
        log.debug("Request to save Initiative : {}", initiative);
        Initiative result = initiativeRepository.save(initiative);
        initiativeSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Initiative> findAll() {
        log.debug("Request to get all Initiatives");
        return initiativeRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Initiative> findOne(Long id) {
        log.debug("Request to get Initiative : {}", id);
        return initiativeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Initiative : {}", id);
        initiativeRepository.deleteById(id);
        initiativeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Initiative> search(String query) {
        log.debug("Request to search Initiatives for query {}", query);
        return StreamSupport
            .stream(initiativeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
