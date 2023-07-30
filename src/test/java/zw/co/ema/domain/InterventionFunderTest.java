package zw.co.ema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import zw.co.ema.web.rest.TestUtil;

class InterventionFunderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterventionFunder.class);
        InterventionFunder interventionFunder1 = new InterventionFunder();
        interventionFunder1.setId(1L);
        InterventionFunder interventionFunder2 = new InterventionFunder();
        interventionFunder2.setId(interventionFunder1.getId());
        assertThat(interventionFunder1).isEqualTo(interventionFunder2);
        interventionFunder2.setId(2L);
        assertThat(interventionFunder1).isNotEqualTo(interventionFunder2);
        interventionFunder1.setId(null);
        assertThat(interventionFunder1).isNotEqualTo(interventionFunder2);
    }
}
