package zw.co.ema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.IntegrationTest;
import zw.co.ema.domain.Funder;
import zw.co.ema.domain.Intervention;
import zw.co.ema.domain.InterventionFunder;
import zw.co.ema.repository.InterventionFunderRepository;

/**
 * Integration tests for the {@link InterventionFunderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterventionFunderResourceIT {

    private static final String ENTITY_API_URL = "/api/intervention-funders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterventionFunderRepository interventionFunderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionFunderMockMvc;

    private InterventionFunder interventionFunder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterventionFunder createEntity(EntityManager em) {
        InterventionFunder interventionFunder = new InterventionFunder();
        return interventionFunder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterventionFunder createUpdatedEntity(EntityManager em) {
        InterventionFunder interventionFunder = new InterventionFunder();
        return interventionFunder;
    }

    @BeforeEach
    public void initTest() {
        interventionFunder = createEntity(em);
    }

    @Test
    @Transactional
    void createInterventionFunder() throws Exception {
        int databaseSizeBeforeCreate = interventionFunderRepository.findAll().size();
        // Create the InterventionFunder
        restInterventionFunderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isCreated());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeCreate + 1);
        InterventionFunder testInterventionFunder = interventionFunderList.get(interventionFunderList.size() - 1);
    }

    @Test
    @Transactional
    void createInterventionFunderWithExistingId() throws Exception {
        // Create the InterventionFunder with an existing ID
        interventionFunder.setId(1L);

        int databaseSizeBeforeCreate = interventionFunderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionFunderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInterventionFunders() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        // Get all the interventionFunderList
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interventionFunder.getId().intValue())));
    }

    @Test
    @Transactional
    void getInterventionFunder() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        // Get the interventionFunder
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL_ID, interventionFunder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interventionFunder.getId().intValue()));
    }

    @Test
    @Transactional
    void getInterventionFundersByIdFiltering() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        Long id = interventionFunder.getId();

        defaultInterventionFunderShouldBeFound("id.equals=" + id);
        defaultInterventionFunderShouldNotBeFound("id.notEquals=" + id);

        defaultInterventionFunderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterventionFunderShouldNotBeFound("id.greaterThan=" + id);

        defaultInterventionFunderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterventionFunderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterventionFundersByInterventionIsEqualToSomething() throws Exception {
        Intervention intervention;
        if (TestUtil.findAll(em, Intervention.class).isEmpty()) {
            interventionFunderRepository.saveAndFlush(interventionFunder);
            intervention = InterventionResourceIT.createEntity(em);
        } else {
            intervention = TestUtil.findAll(em, Intervention.class).get(0);
        }
        em.persist(intervention);
        em.flush();
        interventionFunder.setIntervention(intervention);
        interventionFunderRepository.saveAndFlush(interventionFunder);
        Long interventionId = intervention.getId();
        // Get all the interventionFunderList where intervention equals to interventionId
        defaultInterventionFunderShouldBeFound("interventionId.equals=" + interventionId);

        // Get all the interventionFunderList where intervention equals to (interventionId + 1)
        defaultInterventionFunderShouldNotBeFound("interventionId.equals=" + (interventionId + 1));
    }

    @Test
    @Transactional
    void getAllInterventionFundersByFunderIsEqualToSomething() throws Exception {
        Funder funder;
        if (TestUtil.findAll(em, Funder.class).isEmpty()) {
            interventionFunderRepository.saveAndFlush(interventionFunder);
            funder = FunderResourceIT.createEntity(em);
        } else {
            funder = TestUtil.findAll(em, Funder.class).get(0);
        }
        em.persist(funder);
        em.flush();
        interventionFunder.setFunder(funder);
        interventionFunderRepository.saveAndFlush(interventionFunder);
        Long funderId = funder.getId();
        // Get all the interventionFunderList where funder equals to funderId
        defaultInterventionFunderShouldBeFound("funderId.equals=" + funderId);

        // Get all the interventionFunderList where funder equals to (funderId + 1)
        defaultInterventionFunderShouldNotBeFound("funderId.equals=" + (funderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterventionFunderShouldBeFound(String filter) throws Exception {
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interventionFunder.getId().intValue())));

        // Check, that the count call also returns 1
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterventionFunderShouldNotBeFound(String filter) throws Exception {
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterventionFunderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterventionFunder() throws Exception {
        // Get the interventionFunder
        restInterventionFunderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInterventionFunder() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();

        // Update the interventionFunder
        InterventionFunder updatedInterventionFunder = interventionFunderRepository.findById(interventionFunder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInterventionFunder are not directly saved in db
        em.detach(updatedInterventionFunder);

        restInterventionFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInterventionFunder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInterventionFunder))
            )
            .andExpect(status().isOk());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
        InterventionFunder testInterventionFunder = interventionFunderList.get(interventionFunderList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionFunder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionFunderWithPatch() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();

        // Update the interventionFunder using partial update
        InterventionFunder partialUpdatedInterventionFunder = new InterventionFunder();
        partialUpdatedInterventionFunder.setId(interventionFunder.getId());

        restInterventionFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterventionFunder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterventionFunder))
            )
            .andExpect(status().isOk());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
        InterventionFunder testInterventionFunder = interventionFunderList.get(interventionFunderList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateInterventionFunderWithPatch() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();

        // Update the interventionFunder using partial update
        InterventionFunder partialUpdatedInterventionFunder = new InterventionFunder();
        partialUpdatedInterventionFunder.setId(interventionFunder.getId());

        restInterventionFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterventionFunder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterventionFunder))
            )
            .andExpect(status().isOk());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
        InterventionFunder testInterventionFunder = interventionFunderList.get(interventionFunderList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interventionFunder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterventionFunder() throws Exception {
        int databaseSizeBeforeUpdate = interventionFunderRepository.findAll().size();
        interventionFunder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionFunderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionFunder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterventionFunder in the database
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterventionFunder() throws Exception {
        // Initialize the database
        interventionFunderRepository.saveAndFlush(interventionFunder);

        int databaseSizeBeforeDelete = interventionFunderRepository.findAll().size();

        // Delete the interventionFunder
        restInterventionFunderMockMvc
            .perform(delete(ENTITY_API_URL_ID, interventionFunder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterventionFunder> interventionFunderList = interventionFunderRepository.findAll();
        assertThat(interventionFunderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
