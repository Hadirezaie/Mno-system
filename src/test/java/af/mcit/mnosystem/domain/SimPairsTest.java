package af.mcit.mnosystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import af.mcit.mnosystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimPairsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimPairs.class);
        SimPairs simPairs1 = new SimPairs();
        simPairs1.setId(1L);
        SimPairs simPairs2 = new SimPairs();
        simPairs2.setId(simPairs1.getId());
        assertThat(simPairs1).isEqualTo(simPairs2);
        simPairs2.setId(2L);
        assertThat(simPairs1).isNotEqualTo(simPairs2);
        simPairs1.setId(null);
        assertThat(simPairs1).isNotEqualTo(simPairs2);
    }
}
