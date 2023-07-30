package zw.co.ema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import zw.co.ema.web.rest.TestUtil;

class WardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ward.class);
        Ward ward1 = new Ward();
        ward1.setId(1L);
        Ward ward2 = new Ward();
        ward2.setId(ward1.getId());
        assertThat(ward1).isEqualTo(ward2);
        ward2.setId(2L);
        assertThat(ward1).isNotEqualTo(ward2);
        ward1.setId(null);
        assertThat(ward1).isNotEqualTo(ward2);
    }
}
