package zw.co.ema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import zw.co.ema.web.rest.TestUtil;

class InterventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervention.class);
        Intervention intervention1 = new Intervention();
        intervention1.setId(1L);
        Intervention intervention2 = new Intervention();
        intervention2.setId(intervention1.getId());
        assertThat(intervention1).isEqualTo(intervention2);
        intervention2.setId(2L);
        assertThat(intervention1).isNotEqualTo(intervention2);
        intervention1.setId(null);
        assertThat(intervention1).isNotEqualTo(intervention2);
    }
}
