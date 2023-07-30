package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.InterventionType;
import zw.co.ema.repository.InterventionTypeRepository;

/**
 * Service Implementation for managing {@link InterventionType}.
 */
@Service
@Transactional
public class InterventionTypeService {

    private final Logger log = LoggerFactory.getLogger(InterventionTypeService.class);

    private final InterventionTypeRepository interventionTypeRepository;

    public InterventionTypeService(InterventionTypeRepository interventionTypeRepository) {
        this.interventionTypeRepository = interventionTypeRepository;
    }

    /**
     * Save a interventionType.
     *
     * @param interventionType the entity to save.
     * @return the persisted entity.
     */
    public InterventionType save(InterventionType interventionType) {
        log.debug("Request to save InterventionType : {}", interventionType);
        return interventionTypeRepository.save(interventionType);
    }

    /**
     * Update a interventionType.
     *
     * @param interventionType the entity to save.
     * @return the persisted entity.
     */
    public InterventionType update(InterventionType interventionType) {
        log.debug("Request to update InterventionType : {}", interventionType);
        return interventionTypeRepository.save(interventionType);
    }

    /**
     * Partially update a interventionType.
     *
     * @param interventionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InterventionType> partialUpdate(InterventionType interventionType) {
        log.debug("Request to partially update InterventionType : {}", interventionType);

        return interventionTypeRepository
            .findById(interventionType.getId())
            .map(existingInterventionType -> {
                if (interventionType.getName() != null) {
                    existingInterventionType.setName(interventionType.getName());
                }
                if (interventionType.getCreatedDate() != null) {
                    existingInterventionType.setCreatedDate(interventionType.getCreatedDate());
                }
                if (interventionType.getLastModifiedDate() != null) {
                    existingInterventionType.setLastModifiedDate(interventionType.getLastModifiedDate());
                }

                return existingInterventionType;
            })
            .map(interventionTypeRepository::save);
    }

    /**
     * Get all the interventionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionType> findAll(Pageable pageable) {
        log.debug("Request to get all InterventionTypes");
        return interventionTypeRepository.findAll(pageable);
    }

    /**
     * Get one interventionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InterventionType> findOne(Long id) {
        log.debug("Request to get InterventionType : {}", id);
        return interventionTypeRepository.findById(id);
    }

    /**
     * Delete the interventionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InterventionType : {}", id);
        interventionTypeRepository.deleteById(id);
    }
}
