package com.opencommunity.repository.search;

import com.opencommunity.domain.Volunteer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Volunteer} entity.
 */
public interface VolunteerSearchRepository extends ElasticsearchRepository<Volunteer, Long> {
}
