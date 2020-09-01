package com.opencommunity.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.opencommunity.web.rest.TestUtil;

public class InitiativeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Initiative.class);
        Initiative initiative1 = new Initiative();
        initiative1.setId(1L);
        Initiative initiative2 = new Initiative();
        initiative2.setId(initiative1.getId());
        assertThat(initiative1).isEqualTo(initiative2);
        initiative2.setId(2L);
        assertThat(initiative1).isNotEqualTo(initiative2);
        initiative1.setId(null);
        assertThat(initiative1).isNotEqualTo(initiative2);
    }
}
