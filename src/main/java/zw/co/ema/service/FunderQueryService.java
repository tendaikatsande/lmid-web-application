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
import zw.co.ema.domain.Funder;
import zw.co.ema.repository.FunderRepository;
import zw.co.ema.service.criteria.FunderCriteria;

/**
 * Service for executing complex queries for {@link Funder} entities in the database.
 * The main input is a {@link FunderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Funder} or a {@link Page} of {@link Funder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FunderQueryService extends QueryService<Funder> {

    private final Logger log = LoggerFactory.getLogger(FunderQueryService.class);

    private final FunderRepository funderRepository;

    public FunderQueryService(FunderRepository funderRepository) {
        this.funderRepository = funderRepository;
    }

    /**
     * Return a {@link List} of {@link Funder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Funder> findByCriteria(FunderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Funder> specification = createSpecification(criteria);
        return funderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Funder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Funder> findByCriteria(FunderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Funder> specification = createSpecification(criteria);
        return funderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FunderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Funder> specification = createSpecification(criteria);
        return funderRepository.count(specification);
    }

    /**
     * Function to convert {@link FunderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Funder> createSpecification(FunderCriteria criteria) {
        Specification<Funder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Funder_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Funder_.name));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Funder_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Funder_.lastModifiedDate));
            }
            if (criteria.getSectorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectorId(), root -> root.join(Funder_.sector, JoinType.LEFT).get(Sector_.id))
                    );
            }
        }
        return specification;
    }
}
