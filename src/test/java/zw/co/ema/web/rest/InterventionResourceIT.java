package zw.co.ema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.ema.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.IntegrationTest;
import zw.co.ema.domain.District;
import zw.co.ema.domain.Intervention;
import zw.co.ema.domain.InterventionType;
import zw.co.ema.domain.Project;
import zw.co.ema.domain.Ward;
import zw.co.ema.repository.InterventionRepository;
import zw.co.ema.service.InterventionService;

/**
 * Integration tests for the {@link InterventionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterventionResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TARGET_AREA = 1;
    private static final Integer UPDATED_TARGET_AREA = 2;
    private static final Integer SMALLER_TARGET_AREA = 1 - 1;

    private static final LocalDate DEFAULT_TARGET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARGET_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TARGET_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_ACHIEVED_AREA = 1;
    private static final Integer UPDATED_ACHIEVED_AREA = 2;
    private static final Integer SMALLER_ACHIEVED_AREA = 1 - 1;

    private static final BigDecimal DEFAULT_COST_OF_INTERVENTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST_OF_INTERVENTION = new BigDecimal(2);
    private static final BigDecimal SMALLER_COST_OF_INTERVENTION = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/interventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterventionRepository interventionRepository;

    @Mock
    private InterventionRepository interventionRepositoryMock;

    @Mock
    private InterventionService interventionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .startDate(DEFAULT_START_DATE)
            .targetArea(DEFAULT_TARGET_AREA)
            .targetDate(DEFAULT_TARGET_DATE)
            .achievedArea(DEFAULT_ACHIEVED_AREA)
            .costOfIntervention(DEFAULT_COST_OF_INTERVENTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return intervention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createUpdatedEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .startDate(UPDATED_START_DATE)
            .targetArea(UPDATED_TARGET_AREA)
            .targetDate(UPDATED_TARGET_DATE)
            .achievedArea(UPDATED_ACHIEVED_AREA)
            .costOfIntervention(UPDATED_COST_OF_INTERVENTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return intervention;
    }

    @BeforeEach
    public void initTest() {
        intervention = createEntity(em);
    }

    @Test
    @Transactional
    void createIntervention() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();
        // Create the Intervention
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIntervention.getTargetArea()).isEqualTo(DEFAULT_TARGET_AREA);
        assertThat(testIntervention.getTargetDate()).isEqualTo(DEFAULT_TARGET_DATE);
        assertThat(testIntervention.getAchievedArea()).isEqualTo(DEFAULT_ACHIEVED_AREA);
        assertThat(testIntervention.getCostOfIntervention()).isEqualByComparingTo(DEFAULT_COST_OF_INTERVENTION);
        assertThat(testIntervention.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIntervention.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createInterventionWithExistingId() throws Exception {
        // Create the Intervention with an existing ID
        intervention.setId(1L);

        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInterventions() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetArea").value(hasItem(DEFAULT_TARGET_AREA)))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].achievedArea").value(hasItem(DEFAULT_ACHIEVED_AREA)))
            .andExpect(jsonPath("$.[*].costOfIntervention").value(hasItem(sameNumber(DEFAULT_COST_OF_INTERVENTION))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterventionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(interventionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterventionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(interventionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterventionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(interventionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterventionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(interventionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL_ID, intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.targetArea").value(DEFAULT_TARGET_AREA))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.achievedArea").value(DEFAULT_ACHIEVED_AREA))
            .andExpect(jsonPath("$.costOfIntervention").value(sameNumber(DEFAULT_COST_OF_INTERVENTION)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getInterventionsByIdFiltering() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        Long id = intervention.getId();

        defaultInterventionShouldBeFound("id.equals=" + id);
        defaultInterventionShouldNotBeFound("id.notEquals=" + id);

        defaultInterventionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterventionShouldNotBeFound("id.greaterThan=" + id);

        defaultInterventionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterventionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate equals to DEFAULT_START_DATE
        defaultInterventionShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the interventionList where startDate equals to UPDATED_START_DATE
        defaultInterventionShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultInterventionShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the interventionList where startDate equals to UPDATED_START_DATE
        defaultInterventionShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate is not null
        defaultInterventionShouldBeFound("startDate.specified=true");

        // Get all the interventionList where startDate is null
        defaultInterventionShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultInterventionShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the interventionList where startDate is greater than or equal to UPDATED_START_DATE
        defaultInterventionShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate is less than or equal to DEFAULT_START_DATE
        defaultInterventionShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the interventionList where startDate is less than or equal to SMALLER_START_DATE
        defaultInterventionShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate is less than DEFAULT_START_DATE
        defaultInterventionShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the interventionList where startDate is less than UPDATED_START_DATE
        defaultInterventionShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where startDate is greater than DEFAULT_START_DATE
        defaultInterventionShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the interventionList where startDate is greater than SMALLER_START_DATE
        defaultInterventionShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea equals to DEFAULT_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.equals=" + DEFAULT_TARGET_AREA);

        // Get all the interventionList where targetArea equals to UPDATED_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.equals=" + UPDATED_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea in DEFAULT_TARGET_AREA or UPDATED_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.in=" + DEFAULT_TARGET_AREA + "," + UPDATED_TARGET_AREA);

        // Get all the interventionList where targetArea equals to UPDATED_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.in=" + UPDATED_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea is not null
        defaultInterventionShouldBeFound("targetArea.specified=true");

        // Get all the interventionList where targetArea is null
        defaultInterventionShouldNotBeFound("targetArea.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea is greater than or equal to DEFAULT_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.greaterThanOrEqual=" + DEFAULT_TARGET_AREA);

        // Get all the interventionList where targetArea is greater than or equal to UPDATED_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.greaterThanOrEqual=" + UPDATED_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea is less than or equal to DEFAULT_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.lessThanOrEqual=" + DEFAULT_TARGET_AREA);

        // Get all the interventionList where targetArea is less than or equal to SMALLER_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.lessThanOrEqual=" + SMALLER_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea is less than DEFAULT_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.lessThan=" + DEFAULT_TARGET_AREA);

        // Get all the interventionList where targetArea is less than UPDATED_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.lessThan=" + UPDATED_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetArea is greater than DEFAULT_TARGET_AREA
        defaultInterventionShouldNotBeFound("targetArea.greaterThan=" + DEFAULT_TARGET_AREA);

        // Get all the interventionList where targetArea is greater than SMALLER_TARGET_AREA
        defaultInterventionShouldBeFound("targetArea.greaterThan=" + SMALLER_TARGET_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate equals to DEFAULT_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.equals=" + DEFAULT_TARGET_DATE);

        // Get all the interventionList where targetDate equals to UPDATED_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate in DEFAULT_TARGET_DATE or UPDATED_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE);

        // Get all the interventionList where targetDate equals to UPDATED_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.in=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate is not null
        defaultInterventionShouldBeFound("targetDate.specified=true");

        // Get all the interventionList where targetDate is null
        defaultInterventionShouldNotBeFound("targetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate is greater than or equal to DEFAULT_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.greaterThanOrEqual=" + DEFAULT_TARGET_DATE);

        // Get all the interventionList where targetDate is greater than or equal to UPDATED_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.greaterThanOrEqual=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate is less than or equal to DEFAULT_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.lessThanOrEqual=" + DEFAULT_TARGET_DATE);

        // Get all the interventionList where targetDate is less than or equal to SMALLER_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.lessThanOrEqual=" + SMALLER_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate is less than DEFAULT_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.lessThan=" + DEFAULT_TARGET_DATE);

        // Get all the interventionList where targetDate is less than UPDATED_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.lessThan=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByTargetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where targetDate is greater than DEFAULT_TARGET_DATE
        defaultInterventionShouldNotBeFound("targetDate.greaterThan=" + DEFAULT_TARGET_DATE);

        // Get all the interventionList where targetDate is greater than SMALLER_TARGET_DATE
        defaultInterventionShouldBeFound("targetDate.greaterThan=" + SMALLER_TARGET_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea equals to DEFAULT_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.equals=" + DEFAULT_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea equals to UPDATED_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.equals=" + UPDATED_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea in DEFAULT_ACHIEVED_AREA or UPDATED_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.in=" + DEFAULT_ACHIEVED_AREA + "," + UPDATED_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea equals to UPDATED_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.in=" + UPDATED_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea is not null
        defaultInterventionShouldBeFound("achievedArea.specified=true");

        // Get all the interventionList where achievedArea is null
        defaultInterventionShouldNotBeFound("achievedArea.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea is greater than or equal to DEFAULT_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.greaterThanOrEqual=" + DEFAULT_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea is greater than or equal to UPDATED_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.greaterThanOrEqual=" + UPDATED_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea is less than or equal to DEFAULT_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.lessThanOrEqual=" + DEFAULT_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea is less than or equal to SMALLER_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.lessThanOrEqual=" + SMALLER_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea is less than DEFAULT_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.lessThan=" + DEFAULT_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea is less than UPDATED_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.lessThan=" + UPDATED_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByAchievedAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where achievedArea is greater than DEFAULT_ACHIEVED_AREA
        defaultInterventionShouldNotBeFound("achievedArea.greaterThan=" + DEFAULT_ACHIEVED_AREA);

        // Get all the interventionList where achievedArea is greater than SMALLER_ACHIEVED_AREA
        defaultInterventionShouldBeFound("achievedArea.greaterThan=" + SMALLER_ACHIEVED_AREA);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention equals to DEFAULT_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.equals=" + DEFAULT_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention equals to UPDATED_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.equals=" + UPDATED_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention in DEFAULT_COST_OF_INTERVENTION or UPDATED_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.in=" + DEFAULT_COST_OF_INTERVENTION + "," + UPDATED_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention equals to UPDATED_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.in=" + UPDATED_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention is not null
        defaultInterventionShouldBeFound("costOfIntervention.specified=true");

        // Get all the interventionList where costOfIntervention is null
        defaultInterventionShouldNotBeFound("costOfIntervention.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention is greater than or equal to DEFAULT_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.greaterThanOrEqual=" + DEFAULT_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention is greater than or equal to UPDATED_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.greaterThanOrEqual=" + UPDATED_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention is less than or equal to DEFAULT_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.lessThanOrEqual=" + DEFAULT_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention is less than or equal to SMALLER_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.lessThanOrEqual=" + SMALLER_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsLessThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention is less than DEFAULT_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.lessThan=" + DEFAULT_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention is less than UPDATED_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.lessThan=" + UPDATED_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCostOfInterventionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where costOfIntervention is greater than DEFAULT_COST_OF_INTERVENTION
        defaultInterventionShouldNotBeFound("costOfIntervention.greaterThan=" + DEFAULT_COST_OF_INTERVENTION);

        // Get all the interventionList where costOfIntervention is greater than SMALLER_COST_OF_INTERVENTION
        defaultInterventionShouldBeFound("costOfIntervention.greaterThan=" + SMALLER_COST_OF_INTERVENTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultInterventionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the interventionList where createdDate equals to UPDATED_CREATED_DATE
        defaultInterventionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultInterventionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the interventionList where createdDate equals to UPDATED_CREATED_DATE
        defaultInterventionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where createdDate is not null
        defaultInterventionShouldBeFound("createdDate.specified=true");

        // Get all the interventionList where createdDate is null
        defaultInterventionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultInterventionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the interventionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultInterventionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultInterventionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the interventionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultInterventionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllInterventionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where lastModifiedDate is not null
        defaultInterventionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the interventionList where lastModifiedDate is null
        defaultInterventionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByTypeIsEqualToSomething() throws Exception {
        InterventionType type;
        if (TestUtil.findAll(em, InterventionType.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            type = InterventionTypeResourceIT.createEntity(em);
        } else {
            type = TestUtil.findAll(em, InterventionType.class).get(0);
        }
        em.persist(type);
        em.flush();
        intervention.setType(type);
        interventionRepository.saveAndFlush(intervention);
        Long typeId = type.getId();
        // Get all the interventionList where type equals to typeId
        defaultInterventionShouldBeFound("typeId.equals=" + typeId);

        // Get all the interventionList where type equals to (typeId + 1)
        defaultInterventionShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllInterventionsByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        intervention.setProject(project);
        interventionRepository.saveAndFlush(intervention);
        Long projectId = project.getId();
        // Get all the interventionList where project equals to projectId
        defaultInterventionShouldBeFound("projectId.equals=" + projectId);

        // Get all the interventionList where project equals to (projectId + 1)
        defaultInterventionShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllInterventionsByLocationIsEqualToSomething() throws Exception {
        District location;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            location = DistrictResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(location);
        em.flush();
        intervention.setLocation(location);
        interventionRepository.saveAndFlush(intervention);
        Long locationId = location.getId();
        // Get all the interventionList where location equals to locationId
        defaultInterventionShouldBeFound("locationId.equals=" + locationId);

        // Get all the interventionList where location equals to (locationId + 1)
        defaultInterventionShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllInterventionsByWardIsEqualToSomething() throws Exception {
        Ward ward;
        if (TestUtil.findAll(em, Ward.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            ward = WardResourceIT.createEntity(em);
        } else {
            ward = TestUtil.findAll(em, Ward.class).get(0);
        }
        em.persist(ward);
        em.flush();
        intervention.setWard(ward);
        interventionRepository.saveAndFlush(intervention);
        Long wardId = ward.getId();
        // Get all the interventionList where ward equals to wardId
        defaultInterventionShouldBeFound("wardId.equals=" + wardId);

        // Get all the interventionList where ward equals to (wardId + 1)
        defaultInterventionShouldNotBeFound("wardId.equals=" + (wardId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterventionShouldBeFound(String filter) throws Exception {
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].targetArea").value(hasItem(DEFAULT_TARGET_AREA)))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].achievedArea").value(hasItem(DEFAULT_ACHIEVED_AREA)))
            .andExpect(jsonPath("$.[*].costOfIntervention").value(hasItem(sameNumber(DEFAULT_COST_OF_INTERVENTION))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterventionShouldNotBeFound(String filter) throws Exception {
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findById(intervention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntervention are not directly saved in db
        em.detach(updatedIntervention);
        updatedIntervention
            .startDate(UPDATED_START_DATE)
            .targetArea(UPDATED_TARGET_AREA)
            .targetDate(UPDATED_TARGET_DATE)
            .achievedArea(UPDATED_ACHIEVED_AREA)
            .costOfIntervention(UPDATED_COST_OF_INTERVENTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntervention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testIntervention.getTargetArea()).isEqualTo(UPDATED_TARGET_AREA);
        assertThat(testIntervention.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testIntervention.getAchievedArea()).isEqualTo(UPDATED_ACHIEVED_AREA);
        assertThat(testIntervention.getCostOfIntervention()).isEqualByComparingTo(UPDATED_COST_OF_INTERVENTION);
        assertThat(testIntervention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIntervention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .targetDate(UPDATED_TARGET_DATE)
            .achievedArea(UPDATED_ACHIEVED_AREA)
            .costOfIntervention(UPDATED_COST_OF_INTERVENTION);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIntervention.getTargetArea()).isEqualTo(DEFAULT_TARGET_AREA);
        assertThat(testIntervention.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testIntervention.getAchievedArea()).isEqualTo(UPDATED_ACHIEVED_AREA);
        assertThat(testIntervention.getCostOfIntervention()).isEqualByComparingTo(UPDATED_COST_OF_INTERVENTION);
        assertThat(testIntervention.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIntervention.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .startDate(UPDATED_START_DATE)
            .targetArea(UPDATED_TARGET_AREA)
            .targetDate(UPDATED_TARGET_DATE)
            .achievedArea(UPDATED_ACHIEVED_AREA)
            .costOfIntervention(UPDATED_COST_OF_INTERVENTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testIntervention.getTargetArea()).isEqualTo(UPDATED_TARGET_AREA);
        assertThat(testIntervention.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testIntervention.getAchievedArea()).isEqualTo(UPDATED_ACHIEVED_AREA);
        assertThat(testIntervention.getCostOfIntervention()).isEqualByComparingTo(UPDATED_COST_OF_INTERVENTION);
        assertThat(testIntervention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIntervention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeDelete = interventionRepository.findAll().size();

        // Delete the intervention
        restInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
