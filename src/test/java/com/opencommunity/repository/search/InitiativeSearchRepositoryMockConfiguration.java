package com.opencommunity.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link InitiativeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InitiativeSearchRepositoryMockConfiguration {

    @MockBean
    private InitiativeSearchRepository mockInitiativeSearchRepository;

}
