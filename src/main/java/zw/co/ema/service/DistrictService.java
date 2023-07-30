package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.District;
import zw.co.ema.repository.DistrictRepository;

/**
 * Service Implementation for managing {@link District}.
 */
@Service
@Transactional
public class DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictService.class);

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    /**
     * Save a district.
     *
     * @param district the entity to save.
     * @return the persisted entity.
     */
    public District save(District district) {
        log.debug("Request to save District : {}", district);
        return districtRepository.save(district);
    }

    /**
     * Update a district.
     *
     * @param district the entity to save.
     * @return the persisted entity.
     */
    public District update(District district) {
        log.debug("Request to update District : {}", district);
        return districtRepository.save(district);
    }

    /**
     * Partially update a district.
     *
     * @param district the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<District> partialUpdate(District district) {
        log.debug("Request to partially update District : {}", district);

        return districtRepository
            .findById(district.getId())
            .map(existingDistrict -> {
                if (district.getName() != null) {
                    existingDistrict.setName(district.getName());
                }
                if (district.getLng() != null) {
                    existingDistrict.setLng(district.getLng());
                }
                if (district.getLat() != null) {
                    existingDistrict.setLat(district.getLat());
                }
                if (district.getCreatedDate() != null) {
                    existingDistrict.setCreatedDate(district.getCreatedDate());
                }
                if (district.getLastModifiedDate() != null) {
                    existingDistrict.setLastModifiedDate(district.getLastModifiedDate());
                }

                return existingDistrict;
            })
            .map(districtRepository::save);
    }

    /**
     * Get all the districts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<District> findAll(Pageable pageable) {
        log.debug("Request to get all Districts");
        return districtRepository.findAll(pageable);
    }

    /**
     * Get all the districts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<District> findAllWithEagerRelationships(Pageable pageable) {
        return districtRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one district by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<District> findOne(Long id) {
        log.debug("Request to get District : {}", id);
        return districtRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the district by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        districtRepository.deleteById(id);
    }
}
