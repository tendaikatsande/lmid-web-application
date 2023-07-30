package zw.co.ema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.IntegrationTest;
import zw.co.ema.domain.Funder;
import zw.co.ema.domain.Sector;
import zw.co.ema.repository.FunderRepository;
import zw.co.ema.service.FunderService;
import zw.co.ema.service.criteria.FunderCriteria;

/**
 * Integration tests for the {@link FunderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FunderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/funders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunderRepository funderRepository;

    @Mock
    private FunderRepository funderRepositoryMock;

    @Mock
    private FunderService funderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunderMockMvc;

    private Funder funder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funder createEntity(EntityManager em) {
        Funder funder = new Funder().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE).lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return funder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funder createUpdatedEntity(EntityManager em) {
        Funder funder = new Funder().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return funder;
    }

    @BeforeEach
    public void initTest() {
        funder = createEntity(em);
    }

    @Test
    @Transactional
    void createFunder() throws Exception {
        int databaseSizeBeforeCreate = funderRepository.findAll().size();
        // Create the Funder
        restFunderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funder)))
            .andExpect(status().isCreated());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeCreate + 1);
        Funder testFunder = funderList.get(funderList.size() - 1);
        assertThat(testFunder.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFunder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFunder.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createFunderWithExistingId() throws Exception {
        // Create the Funder with an existing ID
        funder.setId(1L);

        int databaseSizeBeforeCreate = funderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funder)))
            .andExpect(status().isBadRequest());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = funderRepository.findAll().size();
        // set the field null
        funder.setName(null);

        // Create the Funder, which fails.

        restFunderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funder)))
            .andExpect(status().isBadRequest());

        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFunders() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList
        restFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFundersWithEagerRelationshipsIsEnabled() throws Exception {
        when(funderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFunderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(funderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFundersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(funderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFunderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(funderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFunder() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get the funder
        restFunderMockMvc
            .perform(get(ENTITY_API_URL_ID, funder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFundersByIdFiltering() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        Long id = funder.getId();

        defaultFunderShouldBeFound("id.equals=" + id);
        defaultFunderShouldNotBeFound("id.notEquals=" + id);

        defaultFunderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFunderShouldNotBeFound("id.greaterThan=" + id);

        defaultFunderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFunderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFundersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where name equals to DEFAULT_NAME
        defaultFunderShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the funderList where name equals to UPDATED_NAME
        defaultFunderShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFundersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFunderShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the funderList where name equals to UPDATED_NAME
        defaultFunderShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFundersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where name is not null
        defaultFunderShouldBeFound("name.specified=true");

        // Get all the funderList where name is null
        defaultFunderShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFundersByNameContainsSomething() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where name contains DEFAULT_NAME
        defaultFunderShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the funderList where name contains UPDATED_NAME
        defaultFunderShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFundersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where name does not contain DEFAULT_NAME
        defaultFunderShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the funderList where name does not contain UPDATED_NAME
        defaultFunderShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFundersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where createdDate equals to DEFAULT_CREATED_DATE
        defaultFunderShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the funderList where createdDate equals to UPDATED_CREATED_DATE
        defaultFunderShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFundersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultFunderShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the funderList where createdDate equals to UPDATED_CREATED_DATE
        defaultFunderShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFundersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where createdDate is not null
        defaultFunderShouldBeFound("createdDate.specified=true");

        // Get all the funderList where createdDate is null
        defaultFunderShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFundersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultFunderShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the funderList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultFunderShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllFundersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultFunderShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the funderList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultFunderShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllFundersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        // Get all the funderList where lastModifiedDate is not null
        defaultFunderShouldBeFound("lastModifiedDate.specified=true");

        // Get all the funderList where lastModifiedDate is null
        defaultFunderShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFundersBySectorIsEqualToSomething() throws Exception {
        Sector sector;
        if (TestUtil.findAll(em, Sector.class).isEmpty()) {
            funderRepository.saveAndFlush(funder);
            sector = SectorResourceIT.createEntity(em);
        } else {
            sector = TestUtil.findAll(em, Sector.class).get(0);
        }
        em.persist(sector);
        em.flush();
        funder.setSector(sector);
        funderRepository.saveAndFlush(funder);
        Long sectorId = sector.getId();
        // Get all the funderList where sector equals to sectorId
        defaultFunderShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the funderList where sector equals to (sectorId + 1)
        defaultFunderShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFunderShouldBeFound(String filter) throws Exception {
        restFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFunderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFunderShouldNotBeFound(String filter) throws Exception {
        restFunderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFunderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFunder() throws Exception {
        // Get the funder
        restFunderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFunder() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        int databaseSizeBeforeUpdate = funderRepository.findAll().size();

        // Update the funder
        Funder updatedFunder = funderRepository.findById(funder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFunder are not directly saved in db
        em.detach(updatedFunder);
        updatedFunder.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFunder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFunder))
            )
            .andExpect(status().isOk());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
        Funder testFunder = funderList.get(funderList.size() - 1);
        assertThat(testFunder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFunder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFunder.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunderWithPatch() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        int databaseSizeBeforeUpdate = funderRepository.findAll().size();

        // Update the funder using partial update
        Funder partialUpdatedFunder = new Funder();
        partialUpdatedFunder.setId(funder.getId());

        partialUpdatedFunder.name(UPDATED_NAME);

        restFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunder))
            )
            .andExpect(status().isOk());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
        Funder testFunder = funderList.get(funderList.size() - 1);
        assertThat(testFunder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFunder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFunder.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFunderWithPatch() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        int databaseSizeBeforeUpdate = funderRepository.findAll().size();

        // Update the funder using partial update
        Funder partialUpdatedFunder = new Funder();
        partialUpdatedFunder.setId(funder.getId());

        partialUpdatedFunder.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunder))
            )
            .andExpect(status().isOk());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
        Funder testFunder = funderList.get(funderList.size() - 1);
        assertThat(testFunder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFunder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFunder.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunder() throws Exception {
        int databaseSizeBeforeUpdate = funderRepository.findAll().size();
        funder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funder in the database
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFunder() throws Exception {
        // Initialize the database
        funderRepository.saveAndFlush(funder);

        int databaseSizeBeforeDelete = funderRepository.findAll().size();

        // Delete the funder
        restFunderMockMvc
            .perform(delete(ENTITY_API_URL_ID, funder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Funder> funderList = funderRepository.findAll();
        assertThat(funderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
