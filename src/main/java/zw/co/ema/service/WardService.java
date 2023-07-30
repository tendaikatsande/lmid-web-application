package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.Ward;
import zw.co.ema.repository.WardRepository;

/**
 * Service Implementation for managing {@link Ward}.
 */
@Service
@Transactional
public class WardService {

    private final Logger log = LoggerFactory.getLogger(WardService.class);

    private final WardRepository wardRepository;

    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    /**
     * Save a ward.
     *
     * @param ward the entity to save.
     * @return the persisted entity.
     */
    public Ward save(Ward ward) {
        log.debug("Request to save Ward : {}", ward);
        return wardRepository.save(ward);
    }

    /**
     * Update a ward.
     *
     * @param ward the entity to save.
     * @return the persisted entity.
     */
    public Ward update(Ward ward) {
        log.debug("Request to update Ward : {}", ward);
        return wardRepository.save(ward);
    }

    /**
     * Partially update a ward.
     *
     * @param ward the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ward> partialUpdate(Ward ward) {
        log.debug("Request to partially update Ward : {}", ward);

        return wardRepository
            .findById(ward.getId())
            .map(existingWard -> {
                if (ward.getName() != null) {
                    existingWard.setName(ward.getName());
                }
                if (ward.getLng() != null) {
                    existingWard.setLng(ward.getLng());
                }
                if (ward.getLat() != null) {
                    existingWard.setLat(ward.getLat());
                }
                if (ward.getCreatedDate() != null) {
                    existingWard.setCreatedDate(ward.getCreatedDate());
                }
                if (ward.getLastModifiedDate() != null) {
                    existingWard.setLastModifiedDate(ward.getLastModifiedDate());
                }

                return existingWard;
            })
            .map(wardRepository::save);
    }

    /**
     * Get all the wards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Ward> findAll(Pageable pageable) {
        log.debug("Request to get all Wards");
        return wardRepository.findAll(pageable);
    }

    /**
     * Get all the wards with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Ward> findAllWithEagerRelationships(Pageable pageable) {
        return wardRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one ward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ward> findOne(Long id) {
        log.debug("Request to get Ward : {}", id);
        return wardRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the ward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ward : {}", id);
        wardRepository.deleteById(id);
    }
}
