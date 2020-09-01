package com.opencommunity.service.impl;

import com.opencommunity.service.PhotoService;
import com.opencommunity.domain.Photo;
import com.opencommunity.repository.PhotoRepository;
import com.opencommunity.repository.search.PhotoSearchRepository;
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
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final PhotoSearchRepository photoSearchRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoSearchRepository photoSearchRepository) {
        this.photoRepository = photoRepository;
        this.photoSearchRepository = photoSearchRepository;
    }

    @Override
    public Photo save(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        Photo result = photoRepository.save(photo);
        photoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Photo> findAll() {
        log.debug("Request to get all Photos");
        return photoRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Photo> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
        photoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Photo> search(String query) {
        log.debug("Request to search Photos for query {}", query);
        return StreamSupport
            .stream(photoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
