package com.opencommunity.repository.search;

import com.opencommunity.domain.ProjectHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ProjectHistory} entity.
 */
public interface ProjectHistorySearchRepository extends ElasticsearchRepository<ProjectHistory, Long> {
}
