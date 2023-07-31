package zw.co.ema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import zw.co.ema.domain.ApplicationUser;
import zw.co.ema.domain.District;
import zw.co.ema.domain.Province;
import zw.co.ema.domain.User;
import zw.co.ema.repository.ApplicationUserRepository;
import zw.co.ema.service.ApplicationUserService;

/**
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final Long DEFAULT_PROVINCE_ID = 1L;
    private static final Long UPDATED_PROVINCE_ID = 2L;
    private static final Long SMALLER_PROVINCE_ID = 1L - 1L;

    private static final Long DEFAULT_DISTRICT_ID = 1L;
    private static final Long UPDATED_DISTRICT_ID = 2L;
    private static final Long SMALLER_DISTRICT_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    @Mock
    private ApplicationUserService applicationUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser().provinceId(DEFAULT_PROVINCE_ID).districtId(DEFAULT_DISTRICT_ID);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser().provinceId(UPDATED_PROVINCE_ID).districtId(UPDATED_DISTRICT_ID);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getProvinceId()).isEqualTo(DEFAULT_PROVINCE_ID);
        assertThat(testApplicationUser.getDistrictId()).isEqualTo(DEFAULT_DISTRICT_ID);
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(applicationUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.provinceId").value(DEFAULT_PROVINCE_ID.intValue()))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID.intValue()));
    }

    @Test
    @Transactional
    void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserShouldBeFound("id.equals=" + id);
        defaultApplicationUserShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId equals to DEFAULT_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.equals=" + DEFAULT_PROVINCE_ID);

        // Get all the applicationUserList where provinceId equals to UPDATED_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId in DEFAULT_PROVINCE_ID or UPDATED_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID);

        // Get all the applicationUserList where provinceId equals to UPDATED_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.in=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId is not null
        defaultApplicationUserShouldBeFound("provinceId.specified=true");

        // Get all the applicationUserList where provinceId is null
        defaultApplicationUserShouldNotBeFound("provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId is greater than or equal to DEFAULT_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID);

        // Get all the applicationUserList where provinceId is greater than or equal to UPDATED_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId is less than or equal to DEFAULT_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID);

        // Get all the applicationUserList where provinceId is less than or equal to SMALLER_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId is less than DEFAULT_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.lessThan=" + DEFAULT_PROVINCE_ID);

        // Get all the applicationUserList where provinceId is less than UPDATED_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.lessThan=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where provinceId is greater than DEFAULT_PROVINCE_ID
        defaultApplicationUserShouldNotBeFound("provinceId.greaterThan=" + DEFAULT_PROVINCE_ID);

        // Get all the applicationUserList where provinceId is greater than SMALLER_PROVINCE_ID
        defaultApplicationUserShouldBeFound("provinceId.greaterThan=" + SMALLER_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId equals to DEFAULT_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.equals=" + DEFAULT_DISTRICT_ID);

        // Get all the applicationUserList where districtId equals to UPDATED_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId in DEFAULT_DISTRICT_ID or UPDATED_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID);

        // Get all the applicationUserList where districtId equals to UPDATED_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.in=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId is not null
        defaultApplicationUserShouldBeFound("districtId.specified=true");

        // Get all the applicationUserList where districtId is null
        defaultApplicationUserShouldNotBeFound("districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId is greater than or equal to DEFAULT_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the applicationUserList where districtId is greater than or equal to UPDATED_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId is less than or equal to DEFAULT_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the applicationUserList where districtId is less than or equal to SMALLER_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId is less than DEFAULT_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.lessThan=" + DEFAULT_DISTRICT_ID);

        // Get all the applicationUserList where districtId is less than UPDATED_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.lessThan=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where districtId is greater than DEFAULT_DISTRICT_ID
        defaultApplicationUserShouldNotBeFound("districtId.greaterThan=" + DEFAULT_DISTRICT_ID);

        // Get all the applicationUserList where districtId is greater than SMALLER_DISTRICT_ID
        defaultApplicationUserShouldBeFound("districtId.greaterThan=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        applicationUser.setUser(user);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long userId = user.getId();
        // Get all the applicationUserList where user equals to userId
        defaultApplicationUserShouldBeFound("userId.equals=" + userId);

        // Get all the applicationUserList where user equals to (userId + 1)
        defaultApplicationUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByProvinceIsEqualToSomething() throws Exception {
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            province = ProvinceResourceIT.createEntity(em);
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        applicationUser.setProvince(province);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long provinceId = province.getId();
        // Get all the applicationUserList where province equals to provinceId
        defaultApplicationUserShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the applicationUserList where province equals to (provinceId + 1)
        defaultApplicationUserShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            applicationUserRepository.saveAndFlush(applicationUser);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        applicationUser.setDistrict(district);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long districtId = district.getId();
        // Get all the applicationUserList where district equals to districtId
        defaultApplicationUserShouldBeFound("districtId.equals=" + districtId);

        // Get all the applicationUserList where district equals to (districtId + 1)
        defaultApplicationUserShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID.intValue())));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser.provinceId(UPDATED_PROVINCE_ID).districtId(UPDATED_DISTRICT_ID);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getProvinceId()).isEqualTo(UPDATED_PROVINCE_ID);
        assertThat(testApplicationUser.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.districtId(UPDATED_DISTRICT_ID);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getProvinceId()).isEqualTo(DEFAULT_PROVINCE_ID);
        assertThat(testApplicationUser.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.provinceId(UPDATED_PROVINCE_ID).districtId(UPDATED_DISTRICT_ID);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getProvinceId()).isEqualTo(UPDATED_PROVINCE_ID);
        assertThat(testApplicationUser.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
