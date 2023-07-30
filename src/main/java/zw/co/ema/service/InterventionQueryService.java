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
import zw.co.ema.domain.Intervention;
import zw.co.ema.repository.InterventionRepository;
import zw.co.ema.service.criteria.InterventionCriteria;

/**
 * Service for executing complex queries for {@link Intervention} entities in the database.
 * The main input is a {@link InterventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Intervention} or a {@link Page} of {@link Intervention} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterventionQueryService extends QueryService<Intervention> {

    private final Logger log = LoggerFactory.getLogger(InterventionQueryService.class);

    private final InterventionRepository interventionRepository;

    public InterventionQueryService(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    /**
     * Return a {@link List} of {@link Intervention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Intervention> findByCriteria(InterventionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Intervention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Intervention> findByCriteria(InterventionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterventionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.count(specification);
    }

    /**
     * Function to convert {@link InterventionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Intervention> createSpecification(InterventionCriteria criteria) {
        Specification<Intervention> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Intervention_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Intervention_.startDate));
            }
            if (criteria.getTargetArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetArea(), Intervention_.targetArea));
            }
            if (criteria.getTargetDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetDate(), Intervention_.targetDate));
            }
            if (criteria.getAchievedArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAchievedArea(), Intervention_.achievedArea));
            }
            if (criteria.getCostOfIntervention() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCostOfIntervention(), Intervention_.costOfIntervention));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Intervention_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Intervention_.lastModifiedDate));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTypeId(),
                            root -> root.join(Intervention_.type, JoinType.LEFT).get(InterventionType_.id)
                        )
                    );
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectId(),
                            root -> root.join(Intervention_.project, JoinType.LEFT).get(Project_.id)
                        )
                    );
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationId(),
                            root -> root.join(Intervention_.location, JoinType.LEFT).get(District_.id)
                        )
                    );
            }
            if (criteria.getWardId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWardId(), root -> root.join(Intervention_.ward, JoinType.LEFT).get(Ward_.id))
                    );
            }
        }
        return specification;
    }
}
