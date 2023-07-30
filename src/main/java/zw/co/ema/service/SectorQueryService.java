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
import zw.co.ema.domain.Sector;
import zw.co.ema.repository.SectorRepository;
import zw.co.ema.service.criteria.SectorCriteria;

/**
 * Service for executing complex queries for {@link Sector} entities in the database.
 * The main input is a {@link SectorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sector} or a {@link Page} of {@link Sector} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SectorQueryService extends QueryService<Sector> {

    private final Logger log = LoggerFactory.getLogger(SectorQueryService.class);

    private final SectorRepository sectorRepository;

    public SectorQueryService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    /**
     * Return a {@link List} of {@link Sector} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sector> findByCriteria(SectorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sector> specification = createSpecification(criteria);
        return sectorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sector} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sector> findByCriteria(SectorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sector> specification = createSpecification(criteria);
        return sectorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SectorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sector> specification = createSpecification(criteria);
        return sectorRepository.count(specification);
    }

    /**
     * Function to convert {@link SectorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sector> createSpecification(SectorCriteria criteria) {
        Specification<Sector> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sector_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Sector_.name));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Sector_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Sector_.lastModifiedDate));
            }
        }
        return specification;
    }
}
