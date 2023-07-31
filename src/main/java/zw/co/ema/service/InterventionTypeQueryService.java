package zw.co.ema.service;

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
import zw.co.ema.domain.InterventionType;
import zw.co.ema.repository.InterventionTypeRepository;
import zw.co.ema.service.criteria.InterventionTypeCriteria;

/**
 * Service for executing complex queries for {@link InterventionType} entities in the database.
 * The main input is a {@link InterventionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterventionType} or a {@link Page} of {@link InterventionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterventionTypeQueryService extends QueryService<InterventionType> {

    private final Logger log = LoggerFactory.getLogger(InterventionTypeQueryService.class);

    private final InterventionTypeRepository interventionTypeRepository;

    public InterventionTypeQueryService(InterventionTypeRepository interventionTypeRepository) {
        this.interventionTypeRepository = interventionTypeRepository;
    }

    /**
     * Return a {@link List} of {@link InterventionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterventionType> findByCriteria(InterventionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InterventionType> specification = createSpecification(criteria);
        return interventionTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InterventionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionType> findByCriteria(InterventionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InterventionType> specification = createSpecification(criteria);
        return interventionTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterventionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InterventionType> specification = createSpecification(criteria);
        return interventionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InterventionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InterventionType> createSpecification(InterventionTypeCriteria criteria) {
        Specification<InterventionType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InterventionType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), InterventionType_.name));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), InterventionType_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), InterventionType_.lastModifiedDate));
            }
        }
        return specification;
    }
}
