package zw.co.ema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import zw.co.ema.domain.InterventionType;
import zw.co.ema.repository.InterventionTypeRepository;
import zw.co.ema.service.criteria.InterventionTypeCriteria;

/**
 * Integration tests for the {@link InterventionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterventionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/intervention-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterventionTypeRepository interventionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionTypeMockMvc;

    private InterventionType interventionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterventionType createEntity(EntityManager em) {
        InterventionType interventionType = new InterventionType()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return interventionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterventionType createUpdatedEntity(EntityManager em) {
        InterventionType interventionType = new InterventionType()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return interventionType;
    }

    @BeforeEach
    public void initTest() {
        interventionType = createEntity(em);
    }

    @Test
    @Transactional
    void createInterventionType() throws Exception {
        int databaseSizeBeforeCreate = interventionTypeRepository.findAll().size();
        // Create the InterventionType
        restInterventionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isCreated());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InterventionType testInterventionType = interventionTypeList.get(interventionTypeList.size() - 1);
        assertThat(testInterventionType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInterventionType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testInterventionType.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createInterventionTypeWithExistingId() throws Exception {
        // Create the InterventionType with an existing ID
        interventionType.setId(1L);

        int databaseSizeBeforeCreate = interventionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionTypeRepository.findAll().size();
        // set the field null
        interventionType.setName(null);

        // Create the InterventionType, which fails.

        restInterventionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterventionTypes() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interventionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getInterventionType() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get the interventionType
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, interventionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interventionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getInterventionTypesByIdFiltering() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        Long id = interventionType.getId();

        defaultInterventionTypeShouldBeFound("id.equals=" + id);
        defaultInterventionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultInterventionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterventionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultInterventionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterventionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where name equals to DEFAULT_NAME
        defaultInterventionTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the interventionTypeList where name equals to UPDATED_NAME
        defaultInterventionTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInterventionTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the interventionTypeList where name equals to UPDATED_NAME
        defaultInterventionTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where name is not null
        defaultInterventionTypeShouldBeFound("name.specified=true");

        // Get all the interventionTypeList where name is null
        defaultInterventionTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where name contains DEFAULT_NAME
        defaultInterventionTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the interventionTypeList where name contains UPDATED_NAME
        defaultInterventionTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where name does not contain DEFAULT_NAME
        defaultInterventionTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the interventionTypeList where name does not contain UPDATED_NAME
        defaultInterventionTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where createdDate equals to DEFAULT_CREATED_DATE
        defaultInterventionTypeShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the interventionTypeList where createdDate equals to UPDATED_CREATED_DATE
        defaultInterventionTypeShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultInterventionTypeShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the interventionTypeList where createdDate equals to UPDATED_CREATED_DATE
        defaultInterventionTypeShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where createdDate is not null
        defaultInterventionTypeShouldBeFound("createdDate.specified=true");

        // Get all the interventionTypeList where createdDate is null
        defaultInterventionTypeShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionTypesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultInterventionTypeShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the interventionTypeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultInterventionTypeShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultInterventionTypeShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the interventionTypeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultInterventionTypeShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionTypesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        // Get all the interventionTypeList where lastModifiedDate is not null
        defaultInterventionTypeShouldBeFound("lastModifiedDate.specified=true");

        // Get all the interventionTypeList where lastModifiedDate is null
        defaultInterventionTypeShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterventionTypeShouldBeFound(String filter) throws Exception {
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interventionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterventionTypeShouldNotBeFound(String filter) throws Exception {
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterventionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterventionType() throws Exception {
        // Get the interventionType
        restInterventionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInterventionType() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();

        // Update the interventionType
        InterventionType updatedInterventionType = interventionTypeRepository.findById(interventionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInterventionType are not directly saved in db
        em.detach(updatedInterventionType);
        updatedInterventionType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInterventionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInterventionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInterventionType))
            )
            .andExpect(status().isOk());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
        InterventionType testInterventionType = interventionTypeList.get(interventionTypeList.size() - 1);
        assertThat(testInterventionType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInterventionType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testInterventionType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionTypeWithPatch() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();

        // Update the interventionType using partial update
        InterventionType partialUpdatedInterventionType = new InterventionType();
        partialUpdatedInterventionType.setId(interventionType.getId());

        partialUpdatedInterventionType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInterventionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterventionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterventionType))
            )
            .andExpect(status().isOk());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
        InterventionType testInterventionType = interventionTypeList.get(interventionTypeList.size() - 1);
        assertThat(testInterventionType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInterventionType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testInterventionType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInterventionTypeWithPatch() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();

        // Update the interventionType using partial update
        InterventionType partialUpdatedInterventionType = new InterventionType();
        partialUpdatedInterventionType.setId(interventionType.getId());

        partialUpdatedInterventionType.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInterventionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterventionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterventionType))
            )
            .andExpect(status().isOk());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
        InterventionType testInterventionType = interventionTypeList.get(interventionTypeList.size() - 1);
        assertThat(testInterventionType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInterventionType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testInterventionType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interventionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterventionType() throws Exception {
        int databaseSizeBeforeUpdate = interventionTypeRepository.findAll().size();
        interventionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterventionType in the database
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterventionType() throws Exception {
        // Initialize the database
        interventionTypeRepository.saveAndFlush(interventionType);

        int databaseSizeBeforeDelete = interventionTypeRepository.findAll().size();

        // Delete the interventionType
        restInterventionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, interventionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterventionType> interventionTypeList = interventionTypeRepository.findAll();
        assertThat(interventionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
