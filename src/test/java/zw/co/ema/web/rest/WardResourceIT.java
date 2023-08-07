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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.IntegrationTest;
import zw.co.ema.domain.District;
import zw.co.ema.domain.Ward;
import zw.co.ema.repository.WardRepository;
import zw.co.ema.service.WardService;

/**
 * Integration tests for the {@link WardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_LNG = 1F;
    private static final Float UPDATED_LNG = 2F;
    private static final Float SMALLER_LNG = 1F - 1F;

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;
    private static final Float SMALLER_LAT = 1F - 1F;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/wards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WardRepository wardRepository;

    @Mock
    private WardRepository wardRepositoryMock;

    @Mock
    private WardService wardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWardMockMvc;

    private Ward ward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ward createEntity(EntityManager em) {
        Ward ward = new Ward()
            .name(DEFAULT_NAME)
            .lng(DEFAULT_LNG)
            .lat(DEFAULT_LAT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return ward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ward createUpdatedEntity(EntityManager em) {
        Ward ward = new Ward()
            .name(UPDATED_NAME)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return ward;
    }

    @BeforeEach
    public void initTest() {
        ward = createEntity(em);
    }

    @Test
    @Transactional
    void createWard() throws Exception {
        int databaseSizeBeforeCreate = wardRepository.findAll().size();
        // Create the Ward
        restWardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isCreated());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeCreate + 1);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWard.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testWard.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testWard.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWard.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createWardWithExistingId() throws Exception {
        // Create the Ward with an existing ID
        ward.setId(1L);

        int databaseSizeBeforeCreate = wardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wardRepository.findAll().size();
        // set the field null
        ward.setName(null);

        // Create the Ward, which fails.

        restWardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLngIsRequired() throws Exception {
        int databaseSizeBeforeTest = wardRepository.findAll().size();
        // set the field null
        ward.setLng(null);

        // Create the Ward, which fails.

        restWardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = wardRepository.findAll().size();
        // set the field null
        ward.setLat(null);

        // Create the Ward, which fails.

        restWardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isBadRequest());

        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWards() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList
        restWardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ward.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(wardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(wardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(wardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(wardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get the ward
        restWardMockMvc
            .perform(get(ENTITY_API_URL_ID, ward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ward.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getWardsByIdFiltering() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        Long id = ward.getId();

        defaultWardShouldBeFound("id.equals=" + id);
        defaultWardShouldNotBeFound("id.notEquals=" + id);

        defaultWardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWardShouldNotBeFound("id.greaterThan=" + id);

        defaultWardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWardsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where name equals to DEFAULT_NAME
        defaultWardShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the wardList where name equals to UPDATED_NAME
        defaultWardShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWardsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWardShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the wardList where name equals to UPDATED_NAME
        defaultWardShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWardsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where name is not null
        defaultWardShouldBeFound("name.specified=true");

        // Get all the wardList where name is null
        defaultWardShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWardsByNameContainsSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where name contains DEFAULT_NAME
        defaultWardShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the wardList where name contains UPDATED_NAME
        defaultWardShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWardsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where name does not contain DEFAULT_NAME
        defaultWardShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the wardList where name does not contain UPDATED_NAME
        defaultWardShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng equals to DEFAULT_LNG
        defaultWardShouldBeFound("lng.equals=" + DEFAULT_LNG);

        // Get all the wardList where lng equals to UPDATED_LNG
        defaultWardShouldNotBeFound("lng.equals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsInShouldWork() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng in DEFAULT_LNG or UPDATED_LNG
        defaultWardShouldBeFound("lng.in=" + DEFAULT_LNG + "," + UPDATED_LNG);

        // Get all the wardList where lng equals to UPDATED_LNG
        defaultWardShouldNotBeFound("lng.in=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng is not null
        defaultWardShouldBeFound("lng.specified=true");

        // Get all the wardList where lng is null
        defaultWardShouldNotBeFound("lng.specified=false");
    }

    @Test
    @Transactional
    void getAllWardsByLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng is greater than or equal to DEFAULT_LNG
        defaultWardShouldBeFound("lng.greaterThanOrEqual=" + DEFAULT_LNG);

        // Get all the wardList where lng is greater than or equal to UPDATED_LNG
        defaultWardShouldNotBeFound("lng.greaterThanOrEqual=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng is less than or equal to DEFAULT_LNG
        defaultWardShouldBeFound("lng.lessThanOrEqual=" + DEFAULT_LNG);

        // Get all the wardList where lng is less than or equal to SMALLER_LNG
        defaultWardShouldNotBeFound("lng.lessThanOrEqual=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsLessThanSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng is less than DEFAULT_LNG
        defaultWardShouldNotBeFound("lng.lessThan=" + DEFAULT_LNG);

        // Get all the wardList where lng is less than UPDATED_LNG
        defaultWardShouldBeFound("lng.lessThan=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lng is greater than DEFAULT_LNG
        defaultWardShouldNotBeFound("lng.greaterThan=" + DEFAULT_LNG);

        // Get all the wardList where lng is greater than SMALLER_LNG
        defaultWardShouldBeFound("lng.greaterThan=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat equals to DEFAULT_LAT
        defaultWardShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the wardList where lat equals to UPDATED_LAT
        defaultWardShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsInShouldWork() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultWardShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the wardList where lat equals to UPDATED_LAT
        defaultWardShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat is not null
        defaultWardShouldBeFound("lat.specified=true");

        // Get all the wardList where lat is null
        defaultWardShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllWardsByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat is greater than or equal to DEFAULT_LAT
        defaultWardShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the wardList where lat is greater than or equal to UPDATED_LAT
        defaultWardShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat is less than or equal to DEFAULT_LAT
        defaultWardShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the wardList where lat is less than or equal to SMALLER_LAT
        defaultWardShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat is less than DEFAULT_LAT
        defaultWardShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the wardList where lat is less than UPDATED_LAT
        defaultWardShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lat is greater than DEFAULT_LAT
        defaultWardShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the wardList where lat is greater than SMALLER_LAT
        defaultWardShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllWardsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where createdDate equals to DEFAULT_CREATED_DATE
        defaultWardShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the wardList where createdDate equals to UPDATED_CREATED_DATE
        defaultWardShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWardsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultWardShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the wardList where createdDate equals to UPDATED_CREATED_DATE
        defaultWardShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWardsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where createdDate is not null
        defaultWardShouldBeFound("createdDate.specified=true");

        // Get all the wardList where createdDate is null
        defaultWardShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWardsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultWardShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the wardList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultWardShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWardsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultWardShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the wardList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultWardShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWardsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        // Get all the wardList where lastModifiedDate is not null
        defaultWardShouldBeFound("lastModifiedDate.specified=true");

        // Get all the wardList where lastModifiedDate is null
        defaultWardShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWardsByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            wardRepository.saveAndFlush(ward);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        ward.setDistrict(district);
        wardRepository.saveAndFlush(ward);
        Long districtId = district.getId();
        // Get all the wardList where district equals to districtId
        defaultWardShouldBeFound("districtId.equals=" + districtId);

        // Get all the wardList where district equals to (districtId + 1)
        defaultWardShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWardShouldBeFound(String filter) throws Exception {
        restWardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ward.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restWardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWardShouldNotBeFound(String filter) throws Exception {
        restWardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWard() throws Exception {
        // Get the ward
        restWardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeUpdate = wardRepository.findAll().size();

        // Update the ward
        Ward updatedWard = wardRepository.findById(ward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWard are not directly saved in db
        em.detach(updatedWard);
        updatedWard
            .name(UPDATED_NAME)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWard))
            )
            .andExpect(status().isOk());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWard.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testWard.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testWard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWard.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ward.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWardWithPatch() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeUpdate = wardRepository.findAll().size();

        // Update the ward using partial update
        Ward partialUpdatedWard = new Ward();
        partialUpdatedWard.setId(ward.getId());

        partialUpdatedWard
            .name(UPDATED_NAME)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWard))
            )
            .andExpect(status().isOk());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWard.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testWard.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testWard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWard.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWardWithPatch() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeUpdate = wardRepository.findAll().size();

        // Update the ward using partial update
        Ward partialUpdatedWard = new Ward();
        partialUpdatedWard.setId(ward.getId());

        partialUpdatedWard
            .name(UPDATED_NAME)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWard))
            )
            .andExpect(status().isOk());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
        Ward testWard = wardList.get(wardList.size() - 1);
        assertThat(testWard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWard.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testWard.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testWard.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWard.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ward))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWard() throws Exception {
        int databaseSizeBeforeUpdate = wardRepository.findAll().size();
        ward.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ward)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ward in the database
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWard() throws Exception {
        // Initialize the database
        wardRepository.saveAndFlush(ward);

        int databaseSizeBeforeDelete = wardRepository.findAll().size();

        // Delete the ward
        restWardMockMvc
            .perform(delete(ENTITY_API_URL_ID, ward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ward> wardList = wardRepository.findAll();
        assertThat(wardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
