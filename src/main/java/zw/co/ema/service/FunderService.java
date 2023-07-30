package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.Funder;
import zw.co.ema.repository.FunderRepository;

/**
 * Service Implementation for managing {@link Funder}.
 */
@Service
@Transactional
public class FunderService {

    private final Logger log = LoggerFactory.getLogger(FunderService.class);

    private final FunderRepository funderRepository;

    public FunderService(FunderRepository funderRepository) {
        this.funderRepository = funderRepository;
    }

    /**
     * Save a funder.
     *
     * @param funder the entity to save.
     * @return the persisted entity.
     */
    public Funder save(Funder funder) {
        log.debug("Request to save Funder : {}", funder);
        return funderRepository.save(funder);
    }

    /**
     * Update a funder.
     *
     * @param funder the entity to save.
     * @return the persisted entity.
     */
    public Funder update(Funder funder) {
        log.debug("Request to update Funder : {}", funder);
        return funderRepository.save(funder);
    }

    /**
     * Partially update a funder.
     *
     * @param funder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Funder> partialUpdate(Funder funder) {
        log.debug("Request to partially update Funder : {}", funder);

        return funderRepository
            .findById(funder.getId())
            .map(existingFunder -> {
                if (funder.getName() != null) {
                    existingFunder.setName(funder.getName());
                }
                if (funder.getCreatedDate() != null) {
                    existingFunder.setCreatedDate(funder.getCreatedDate());
                }
                if (funder.getLastModifiedDate() != null) {
                    existingFunder.setLastModifiedDate(funder.getLastModifiedDate());
                }

                return existingFunder;
            })
            .map(funderRepository::save);
    }

    /**
     * Get all the funders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Funder> findAll(Pageable pageable) {
        log.debug("Request to get all Funders");
        return funderRepository.findAll(pageable);
    }

    /**
     * Get all the funders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Funder> findAllWithEagerRelationships(Pageable pageable) {
        return funderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one funder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Funder> findOne(Long id) {
        log.debug("Request to get Funder : {}", id);
        return funderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the funder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Funder : {}", id);
        funderRepository.deleteById(id);
    }
}
