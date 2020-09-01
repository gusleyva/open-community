package com.opencommunity.repository.search;

import com.opencommunity.domain.Initiative;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Initiative} entity.
 */
public interface InitiativeSearchRepository extends ElasticsearchRepository<Initiative, Long> {
}
