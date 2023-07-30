package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.InterventionFunder;
import zw.co.ema.repository.InterventionFunderRepository;

/**
 * Service Implementation for managing {@link InterventionFunder}.
 */
@Service
@Transactional
public class InterventionFunderService {

    private final Logger log = LoggerFactory.getLogger(InterventionFunderService.class);

    private final InterventionFunderRepository interventionFunderRepository;

    public InterventionFunderService(InterventionFunderRepository interventionFunderRepository) {
        this.interventionFunderRepository = interventionFunderRepository;
    }

    /**
     * Save a interventionFunder.
     *
     * @param interventionFunder the entity to save.
     * @return the persisted entity.
     */
    public InterventionFunder save(InterventionFunder interventionFunder) {
        log.debug("Request to save InterventionFunder : {}", interventionFunder);
        return interventionFunderRepository.save(interventionFunder);
    }

    /**
     * Update a interventionFunder.
     *
     * @param interventionFunder the entity to save.
     * @return the persisted entity.
     */
    public InterventionFunder update(InterventionFunder interventionFunder) {
        log.debug("Request to update InterventionFunder : {}", interventionFunder);
        return interventionFunderRepository.save(interventionFunder);
    }

    /**
     * Partially update a interventionFunder.
     *
     * @param interventionFunder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InterventionFunder> partialUpdate(InterventionFunder interventionFunder) {
        log.debug("Request to partially update InterventionFunder : {}", interventionFunder);

        return interventionFunderRepository
            .findById(interventionFunder.getId())
            .map(existingInterventionFunder -> {
                return existingInterventionFunder;
            })
            .map(interventionFunderRepository::save);
    }

    /**
     * Get all the interventionFunders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionFunder> findAll(Pageable pageable) {
        log.debug("Request to get all InterventionFunders");
        return interventionFunderRepository.findAll(pageable);
    }

    /**
     * Get one interventionFunder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InterventionFunder> findOne(Long id) {
        log.debug("Request to get InterventionFunder : {}", id);
        return interventionFunderRepository.findById(id);
    }

    /**
     * Delete the interventionFunder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InterventionFunder : {}", id);
        interventionFunderRepository.deleteById(id);
    }
}
