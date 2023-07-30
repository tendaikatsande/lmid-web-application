package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.Intervention;
import zw.co.ema.repository.InterventionRepository;

/**
 * Service Implementation for managing {@link Intervention}.
 */
@Service
@Transactional
public class InterventionService {

    private final Logger log = LoggerFactory.getLogger(InterventionService.class);

    private final InterventionRepository interventionRepository;

    public InterventionService(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    /**
     * Save a intervention.
     *
     * @param intervention the entity to save.
     * @return the persisted entity.
     */
    public Intervention save(Intervention intervention) {
        log.debug("Request to save Intervention : {}", intervention);
        return interventionRepository.save(intervention);
    }

    /**
     * Update a intervention.
     *
     * @param intervention the entity to save.
     * @return the persisted entity.
     */
    public Intervention update(Intervention intervention) {
        log.debug("Request to update Intervention : {}", intervention);
        return interventionRepository.save(intervention);
    }

    /**
     * Partially update a intervention.
     *
     * @param intervention the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Intervention> partialUpdate(Intervention intervention) {
        log.debug("Request to partially update Intervention : {}", intervention);

        return interventionRepository
            .findById(intervention.getId())
            .map(existingIntervention -> {
                if (intervention.getStartDate() != null) {
                    existingIntervention.setStartDate(intervention.getStartDate());
                }
                if (intervention.getTargetArea() != null) {
                    existingIntervention.setTargetArea(intervention.getTargetArea());
                }
                if (intervention.getTargetDate() != null) {
                    existingIntervention.setTargetDate(intervention.getTargetDate());
                }
                if (intervention.getAchievedArea() != null) {
                    existingIntervention.setAchievedArea(intervention.getAchievedArea());
                }
                if (intervention.getCostOfIntervention() != null) {
                    existingIntervention.setCostOfIntervention(intervention.getCostOfIntervention());
                }
                if (intervention.getCreatedDate() != null) {
                    existingIntervention.setCreatedDate(intervention.getCreatedDate());
                }
                if (intervention.getLastModifiedDate() != null) {
                    existingIntervention.setLastModifiedDate(intervention.getLastModifiedDate());
                }

                return existingIntervention;
            })
            .map(interventionRepository::save);
    }

    /**
     * Get all the interventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Intervention> findAll(Pageable pageable) {
        log.debug("Request to get all Interventions");
        return interventionRepository.findAll(pageable);
    }

    /**
     * Get all the interventions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Intervention> findAllWithEagerRelationships(Pageable pageable) {
        return interventionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one intervention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Intervention> findOne(Long id) {
        log.debug("Request to get Intervention : {}", id);
        return interventionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the intervention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Intervention : {}", id);
        interventionRepository.deleteById(id);
    }
}
