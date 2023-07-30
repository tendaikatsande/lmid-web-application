package zw.co.ema.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.ema.domain.Sector;
import zw.co.ema.repository.SectorRepository;

/**
 * Service Implementation for managing {@link Sector}.
 */
@Service
@Transactional
public class SectorService {

    private final Logger log = LoggerFactory.getLogger(SectorService.class);

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    /**
     * Save a sector.
     *
     * @param sector the entity to save.
     * @return the persisted entity.
     */
    public Sector save(Sector sector) {
        log.debug("Request to save Sector : {}", sector);
        return sectorRepository.save(sector);
    }

    /**
     * Update a sector.
     *
     * @param sector the entity to save.
     * @return the persisted entity.
     */
    public Sector update(Sector sector) {
        log.debug("Request to update Sector : {}", sector);
        return sectorRepository.save(sector);
    }

    /**
     * Partially update a sector.
     *
     * @param sector the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Sector> partialUpdate(Sector sector) {
        log.debug("Request to partially update Sector : {}", sector);

        return sectorRepository
            .findById(sector.getId())
            .map(existingSector -> {
                if (sector.getName() != null) {
                    existingSector.setName(sector.getName());
                }
                if (sector.getCreatedDate() != null) {
                    existingSector.setCreatedDate(sector.getCreatedDate());
                }
                if (sector.getLastModifiedDate() != null) {
                    existingSector.setLastModifiedDate(sector.getLastModifiedDate());
                }

                return existingSector;
            })
            .map(sectorRepository::save);
    }

    /**
     * Get all the sectors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Sector> findAll(Pageable pageable) {
        log.debug("Request to get all Sectors");
        return sectorRepository.findAll(pageable);
    }

    /**
     * Get one sector by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Sector> findOne(Long id) {
        log.debug("Request to get Sector : {}", id);
        return sectorRepository.findById(id);
    }

    /**
     * Delete the sector by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sector : {}", id);
        sectorRepository.deleteById(id);
    }
}
