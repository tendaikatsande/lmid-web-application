package zw.co.ema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import zw.co.ema.web.rest.TestUtil;

class InterventionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterventionType.class);
        InterventionType interventionType1 = new InterventionType();
        interventionType1.setId(1L);
        InterventionType interventionType2 = new InterventionType();
        interventionType2.setId(interventionType1.getId());
        assertThat(interventionType1).isEqualTo(interventionType2);
        interventionType2.setId(2L);
        assertThat(interventionType1).isNotEqualTo(interventionType2);
        interventionType1.setId(null);
        assertThat(interventionType1).isNotEqualTo(interventionType2);
    }
}
