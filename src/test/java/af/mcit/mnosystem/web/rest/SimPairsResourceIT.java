package af.mcit.mnosystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.mnosystem.IntegrationTest;
import af.mcit.mnosystem.domain.SimPairs;
import af.mcit.mnosystem.repository.SimPairsRepository;
import af.mcit.mnosystem.service.criteria.SimPairsCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SimPairsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimPairsResourceIT {

    private static final String DEFAULT_MSISDN = "AAAAAAAAAA";
    private static final String UPDATED_MSISDN = "BBBBBBBBBB";

    private static final String DEFAULT_IMSI = "AAAAAAAAAA";
    private static final String UPDATED_IMSI = "BBBBBBBBBB";

    private static final Long DEFAULT_IMEI_NUMBER = 1L;
    private static final Long UPDATED_IMEI_NUMBER = 2L;
    private static final Long SMALLER_IMEI_NUMBER = 1L - 1L;

    private static final Boolean DEFAULT_SENT = false;
    private static final Boolean UPDATED_SENT = true;

    private static final String ENTITY_API_URL = "/api/sim-pairs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SimPairsRepository simPairsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimPairsMockMvc;

    private SimPairs simPairs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimPairs createEntity(EntityManager em) {
        SimPairs simPairs = new SimPairs().msisdn(DEFAULT_MSISDN).imsi(DEFAULT_IMSI).imeiNumber(DEFAULT_IMEI_NUMBER).sent(DEFAULT_SENT);
        return simPairs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimPairs createUpdatedEntity(EntityManager em) {
        SimPairs simPairs = new SimPairs().msisdn(UPDATED_MSISDN).imsi(UPDATED_IMSI).imeiNumber(UPDATED_IMEI_NUMBER).sent(UPDATED_SENT);
        return simPairs;
    }

    @BeforeEach
    public void initTest() {
        simPairs = createEntity(em);
    }

    @Test
    @Transactional
    void createSimPairs() throws Exception {
        int databaseSizeBeforeCreate = simPairsRepository.findAll().size();
        // Create the SimPairs
        restSimPairsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(simPairs)))
            .andExpect(status().isCreated());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeCreate + 1);
        SimPairs testSimPairs = simPairsList.get(simPairsList.size() - 1);
        assertThat(testSimPairs.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testSimPairs.getImsi()).isEqualTo(DEFAULT_IMSI);
        assertThat(testSimPairs.getImeiNumber()).isEqualTo(DEFAULT_IMEI_NUMBER);
        assertThat(testSimPairs.getSent()).isEqualTo(DEFAULT_SENT);
    }

    @Test
    @Transactional
    void createSimPairsWithExistingId() throws Exception {
        // Create the SimPairs with an existing ID
        simPairs.setId(1L);

        int databaseSizeBeforeCreate = simPairsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimPairsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(simPairs)))
            .andExpect(status().isBadRequest());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSimPairs() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simPairs.getId().intValue())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN)))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI)))
            .andExpect(jsonPath("$.[*].imeiNumber").value(hasItem(DEFAULT_IMEI_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT.booleanValue())));
    }

    @Test
    @Transactional
    void getSimPairs() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get the simPairs
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL_ID, simPairs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(simPairs.getId().intValue()))
            .andExpect(jsonPath("$.msisdn").value(DEFAULT_MSISDN))
            .andExpect(jsonPath("$.imsi").value(DEFAULT_IMSI))
            .andExpect(jsonPath("$.imeiNumber").value(DEFAULT_IMEI_NUMBER.intValue()))
            .andExpect(jsonPath("$.sent").value(DEFAULT_SENT.booleanValue()));
    }

    @Test
    @Transactional
    void getSimPairsByIdFiltering() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        Long id = simPairs.getId();

        defaultSimPairsShouldBeFound("id.equals=" + id);
        defaultSimPairsShouldNotBeFound("id.notEquals=" + id);

        defaultSimPairsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSimPairsShouldNotBeFound("id.greaterThan=" + id);

        defaultSimPairsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSimPairsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimPairsByMsisdnIsEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where msisdn equals to DEFAULT_MSISDN
        defaultSimPairsShouldBeFound("msisdn.equals=" + DEFAULT_MSISDN);

        // Get all the simPairsList where msisdn equals to UPDATED_MSISDN
        defaultSimPairsShouldNotBeFound("msisdn.equals=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    void getAllSimPairsByMsisdnIsInShouldWork() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where msisdn in DEFAULT_MSISDN or UPDATED_MSISDN
        defaultSimPairsShouldBeFound("msisdn.in=" + DEFAULT_MSISDN + "," + UPDATED_MSISDN);

        // Get all the simPairsList where msisdn equals to UPDATED_MSISDN
        defaultSimPairsShouldNotBeFound("msisdn.in=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    void getAllSimPairsByMsisdnIsNullOrNotNull() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where msisdn is not null
        defaultSimPairsShouldBeFound("msisdn.specified=true");

        // Get all the simPairsList where msisdn is null
        defaultSimPairsShouldNotBeFound("msisdn.specified=false");
    }

    @Test
    @Transactional
    void getAllSimPairsByMsisdnContainsSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where msisdn contains DEFAULT_MSISDN
        defaultSimPairsShouldBeFound("msisdn.contains=" + DEFAULT_MSISDN);

        // Get all the simPairsList where msisdn contains UPDATED_MSISDN
        defaultSimPairsShouldNotBeFound("msisdn.contains=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    void getAllSimPairsByMsisdnNotContainsSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where msisdn does not contain DEFAULT_MSISDN
        defaultSimPairsShouldNotBeFound("msisdn.doesNotContain=" + DEFAULT_MSISDN);

        // Get all the simPairsList where msisdn does not contain UPDATED_MSISDN
        defaultSimPairsShouldBeFound("msisdn.doesNotContain=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    void getAllSimPairsByImsiIsEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imsi equals to DEFAULT_IMSI
        defaultSimPairsShouldBeFound("imsi.equals=" + DEFAULT_IMSI);

        // Get all the simPairsList where imsi equals to UPDATED_IMSI
        defaultSimPairsShouldNotBeFound("imsi.equals=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    void getAllSimPairsByImsiIsInShouldWork() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imsi in DEFAULT_IMSI or UPDATED_IMSI
        defaultSimPairsShouldBeFound("imsi.in=" + DEFAULT_IMSI + "," + UPDATED_IMSI);

        // Get all the simPairsList where imsi equals to UPDATED_IMSI
        defaultSimPairsShouldNotBeFound("imsi.in=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    void getAllSimPairsByImsiIsNullOrNotNull() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imsi is not null
        defaultSimPairsShouldBeFound("imsi.specified=true");

        // Get all the simPairsList where imsi is null
        defaultSimPairsShouldNotBeFound("imsi.specified=false");
    }

    @Test
    @Transactional
    void getAllSimPairsByImsiContainsSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imsi contains DEFAULT_IMSI
        defaultSimPairsShouldBeFound("imsi.contains=" + DEFAULT_IMSI);

        // Get all the simPairsList where imsi contains UPDATED_IMSI
        defaultSimPairsShouldNotBeFound("imsi.contains=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    void getAllSimPairsByImsiNotContainsSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imsi does not contain DEFAULT_IMSI
        defaultSimPairsShouldNotBeFound("imsi.doesNotContain=" + DEFAULT_IMSI);

        // Get all the simPairsList where imsi does not contain UPDATED_IMSI
        defaultSimPairsShouldBeFound("imsi.doesNotContain=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber equals to DEFAULT_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.equals=" + DEFAULT_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber equals to UPDATED_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.equals=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsInShouldWork() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber in DEFAULT_IMEI_NUMBER or UPDATED_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.in=" + DEFAULT_IMEI_NUMBER + "," + UPDATED_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber equals to UPDATED_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.in=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber is not null
        defaultSimPairsShouldBeFound("imeiNumber.specified=true");

        // Get all the simPairsList where imeiNumber is null
        defaultSimPairsShouldNotBeFound("imeiNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber is greater than or equal to DEFAULT_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.greaterThanOrEqual=" + DEFAULT_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber is greater than or equal to UPDATED_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.greaterThanOrEqual=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber is less than or equal to DEFAULT_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.lessThanOrEqual=" + DEFAULT_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber is less than or equal to SMALLER_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.lessThanOrEqual=" + SMALLER_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber is less than DEFAULT_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.lessThan=" + DEFAULT_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber is less than UPDATED_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.lessThan=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsByImeiNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where imeiNumber is greater than DEFAULT_IMEI_NUMBER
        defaultSimPairsShouldNotBeFound("imeiNumber.greaterThan=" + DEFAULT_IMEI_NUMBER);

        // Get all the simPairsList where imeiNumber is greater than SMALLER_IMEI_NUMBER
        defaultSimPairsShouldBeFound("imeiNumber.greaterThan=" + SMALLER_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllSimPairsBySentIsEqualToSomething() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where sent equals to DEFAULT_SENT
        defaultSimPairsShouldBeFound("sent.equals=" + DEFAULT_SENT);

        // Get all the simPairsList where sent equals to UPDATED_SENT
        defaultSimPairsShouldNotBeFound("sent.equals=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    void getAllSimPairsBySentIsInShouldWork() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where sent in DEFAULT_SENT or UPDATED_SENT
        defaultSimPairsShouldBeFound("sent.in=" + DEFAULT_SENT + "," + UPDATED_SENT);

        // Get all the simPairsList where sent equals to UPDATED_SENT
        defaultSimPairsShouldNotBeFound("sent.in=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    void getAllSimPairsBySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        // Get all the simPairsList where sent is not null
        defaultSimPairsShouldBeFound("sent.specified=true");

        // Get all the simPairsList where sent is null
        defaultSimPairsShouldNotBeFound("sent.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimPairsShouldBeFound(String filter) throws Exception {
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simPairs.getId().intValue())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN)))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI)))
            .andExpect(jsonPath("$.[*].imeiNumber").value(hasItem(DEFAULT_IMEI_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT.booleanValue())));

        // Check, that the count call also returns 1
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimPairsShouldNotBeFound(String filter) throws Exception {
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimPairsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimPairs() throws Exception {
        // Get the simPairs
        restSimPairsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimPairs() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();

        // Update the simPairs
        SimPairs updatedSimPairs = simPairsRepository.findById(simPairs.getId()).get();
        // Disconnect from session so that the updates on updatedSimPairs are not directly saved in db
        em.detach(updatedSimPairs);
        updatedSimPairs.msisdn(UPDATED_MSISDN).imsi(UPDATED_IMSI).imeiNumber(UPDATED_IMEI_NUMBER).sent(UPDATED_SENT);

        restSimPairsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSimPairs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSimPairs))
            )
            .andExpect(status().isOk());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
        SimPairs testSimPairs = simPairsList.get(simPairsList.size() - 1);
        assertThat(testSimPairs.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSimPairs.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testSimPairs.getImeiNumber()).isEqualTo(UPDATED_IMEI_NUMBER);
        assertThat(testSimPairs.getSent()).isEqualTo(UPDATED_SENT);
    }

    @Test
    @Transactional
    void putNonExistingSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, simPairs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simPairs))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simPairs))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(simPairs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSimPairsWithPatch() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();

        // Update the simPairs using partial update
        SimPairs partialUpdatedSimPairs = new SimPairs();
        partialUpdatedSimPairs.setId(simPairs.getId());

        partialUpdatedSimPairs.msisdn(UPDATED_MSISDN).imeiNumber(UPDATED_IMEI_NUMBER).sent(UPDATED_SENT);

        restSimPairsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimPairs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimPairs))
            )
            .andExpect(status().isOk());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
        SimPairs testSimPairs = simPairsList.get(simPairsList.size() - 1);
        assertThat(testSimPairs.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSimPairs.getImsi()).isEqualTo(DEFAULT_IMSI);
        assertThat(testSimPairs.getImeiNumber()).isEqualTo(UPDATED_IMEI_NUMBER);
        assertThat(testSimPairs.getSent()).isEqualTo(UPDATED_SENT);
    }

    @Test
    @Transactional
    void fullUpdateSimPairsWithPatch() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();

        // Update the simPairs using partial update
        SimPairs partialUpdatedSimPairs = new SimPairs();
        partialUpdatedSimPairs.setId(simPairs.getId());

        partialUpdatedSimPairs.msisdn(UPDATED_MSISDN).imsi(UPDATED_IMSI).imeiNumber(UPDATED_IMEI_NUMBER).sent(UPDATED_SENT);

        restSimPairsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimPairs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimPairs))
            )
            .andExpect(status().isOk());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
        SimPairs testSimPairs = simPairsList.get(simPairsList.size() - 1);
        assertThat(testSimPairs.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSimPairs.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testSimPairs.getImeiNumber()).isEqualTo(UPDATED_IMEI_NUMBER);
        assertThat(testSimPairs.getSent()).isEqualTo(UPDATED_SENT);
    }

    @Test
    @Transactional
    void patchNonExistingSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, simPairs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simPairs))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simPairs))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimPairs() throws Exception {
        int databaseSizeBeforeUpdate = simPairsRepository.findAll().size();
        simPairs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimPairsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(simPairs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimPairs in the database
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSimPairs() throws Exception {
        // Initialize the database
        simPairsRepository.saveAndFlush(simPairs);

        int databaseSizeBeforeDelete = simPairsRepository.findAll().size();

        // Delete the simPairs
        restSimPairsMockMvc
            .perform(delete(ENTITY_API_URL_ID, simPairs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SimPairs> simPairsList = simPairsRepository.findAll();
        assertThat(simPairsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
