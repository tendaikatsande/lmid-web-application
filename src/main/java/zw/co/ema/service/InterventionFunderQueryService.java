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
import zw.co.ema.domain.InterventionFunder;
import zw.co.ema.repository.InterventionFunderRepository;
import zw.co.ema.service.criteria.InterventionFunderCriteria;

/**
 * Service for executing complex queries for {@link InterventionFunder} entities in the database.
 * The main input is a {@link InterventionFunderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterventionFunder} or a {@link Page} of {@link InterventionFunder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterventionFunderQueryService extends QueryService<InterventionFunder> {

    private final Logger log = LoggerFactory.getLogger(InterventionFunderQueryService.class);

    private final InterventionFunderRepository interventionFunderRepository;

    public InterventionFunderQueryService(InterventionFunderRepository interventionFunderRepository) {
        this.interventionFunderRepository = interventionFunderRepository;
    }

    /**
     * Return a {@link List} of {@link InterventionFunder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterventionFunder> findByCriteria(InterventionFunderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InterventionFunder> specification = createSpecification(criteria);
        return interventionFunderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InterventionFunder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionFunder> findByCriteria(InterventionFunderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InterventionFunder> specification = createSpecification(criteria);
        return interventionFunderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterventionFunderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InterventionFunder> specification = createSpecification(criteria);
        return interventionFunderRepository.count(specification);
    }

    /**
     * Function to convert {@link InterventionFunderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InterventionFunder> createSpecification(InterventionFunderCriteria criteria) {
        Specification<InterventionFunder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InterventionFunder_.id));
            }
            if (criteria.getInterventionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterventionId(),
                            root -> root.join(InterventionFunder_.intervention, JoinType.LEFT).get(Intervention_.id)
                        )
                    );
            }
            if (criteria.getFunderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFunderId(),
                            root -> root.join(InterventionFunder_.funder, JoinType.LEFT).get(Funder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
