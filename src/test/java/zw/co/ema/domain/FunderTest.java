package zw.co.ema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import zw.co.ema.web.rest.TestUtil;

class FunderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funder.class);
        Funder funder1 = new Funder();
        funder1.setId(1L);
        Funder funder2 = new Funder();
        funder2.setId(funder1.getId());
        assertThat(funder1).isEqualTo(funder2);
        funder2.setId(2L);
        assertThat(funder1).isNotEqualTo(funder2);
        funder1.setId(null);
        assertThat(funder1).isNotEqualTo(funder2);
    }
}
