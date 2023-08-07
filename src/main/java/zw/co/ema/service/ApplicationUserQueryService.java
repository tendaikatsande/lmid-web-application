package zw.co.ema.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import zw.co.ema.domain.*; // for static metamodels
import zw.co.ema.domain.ApplicationUser;
import zw.co.ema.repository.ApplicationUserRepository;
import zw.co.ema.service.criteria.ApplicationUserCriteria;

/**
 * Service for executing complex queries for {@link ApplicationUser} entities in the database.
 * The main input is a {@link ApplicationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApplicationUser} or a {@link Page} of {@link ApplicationUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationUserQueryService extends QueryService<ApplicationUser> {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserQueryService.class);

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserQueryService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Return a {@link List} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationUser> createSpecification(ApplicationUserCriteria criteria) {
        Specification<ApplicationUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ApplicationUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(ApplicationUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvinceId(),
                            root -> root.join(ApplicationUser_.province, JoinType.LEFT).get(Province_.id)
                        )
                    );
            }
            if (criteria.getDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDistrictId(),
                            root -> root.join(ApplicationUser_.district, JoinType.LEFT).get(District_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
