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
import zw.co.ema.domain.Ward;
import zw.co.ema.repository.WardRepository;
import zw.co.ema.service.criteria.WardCriteria;

/**
 * Service for executing complex queries for {@link Ward} entities in the database.
 * The main input is a {@link WardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ward} or a {@link Page} of {@link Ward} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WardQueryService extends QueryService<Ward> {

    private final Logger log = LoggerFactory.getLogger(WardQueryService.class);

    private final WardRepository wardRepository;

    public WardQueryService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    /**
     * Return a {@link List} of {@link Ward} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ward> findByCriteria(WardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ward> specification = createSpecification(criteria);
        return wardRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ward} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ward> findByCriteria(WardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ward> specification = createSpecification(criteria);
        return wardRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ward> specification = createSpecification(criteria);
        return wardRepository.count(specification);
    }

    /**
     * Function to convert {@link WardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ward> createSpecification(WardCriteria criteria) {
        Specification<Ward> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ward_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ward_.name));
            }
            if (criteria.getLng() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLng(), Ward_.lng));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), Ward_.lat));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Ward_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Ward_.lastModifiedDate));
            }
            if (criteria.getDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDistrictId(), root -> root.join(Ward_.district, JoinType.LEFT).get(District_.id))
                    );
            }
        }
        return specification;
    }
}
