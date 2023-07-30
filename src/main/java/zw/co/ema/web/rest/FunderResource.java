package zw.co.ema.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import zw.co.ema.domain.Funder;
import zw.co.ema.repository.FunderRepository;
import zw.co.ema.service.FunderQueryService;
import zw.co.ema.service.FunderService;
import zw.co.ema.service.criteria.FunderCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.Funder}.
 */
@RestController
@RequestMapping("/api")
public class FunderResource {

    private final Logger log = LoggerFactory.getLogger(FunderResource.class);

    private static final String ENTITY_NAME = "funder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FunderService funderService;

    private final FunderRepository funderRepository;

    private final FunderQueryService funderQueryService;

    public FunderResource(FunderService funderService, FunderRepository funderRepository, FunderQueryService funderQueryService) {
        this.funderService = funderService;
        this.funderRepository = funderRepository;
        this.funderQueryService = funderQueryService;
    }

    /**
     * {@code POST  /funders} : Create a new funder.
     *
     * @param funder the funder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funder, or with status {@code 400 (Bad Request)} if the funder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funders")
    public ResponseEntity<Funder> createFunder(@Valid @RequestBody Funder funder) throws URISyntaxException {
        log.debug("REST request to save Funder : {}", funder);
        if (funder.getId() != null) {
            throw new BadRequestAlertException("A new funder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Funder result = funderService.save(funder);
        return ResponseEntity
            .created(new URI("/api/funders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funders/:id} : Updates an existing funder.
     *
     * @param id the id of the funder to save.
     * @param funder the funder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funder,
     * or with status {@code 400 (Bad Request)} if the funder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funders/{id}")
    public ResponseEntity<Funder> updateFunder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Funder funder
    ) throws URISyntaxException {
        log.debug("REST request to update Funder : {}, {}", id, funder);
        if (funder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Funder result = funderService.update(funder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funders/:id} : Partial updates given fields of an existing funder, field will ignore if it is null
     *
     * @param id the id of the funder to save.
     * @param funder the funder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funder,
     * or with status {@code 400 (Bad Request)} if the funder is not valid,
     * or with status {@code 404 (Not Found)} if the funder is not found,
     * or with status {@code 500 (Internal Server Error)} if the funder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Funder> partialUpdateFunder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Funder funder
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funder partially : {}, {}", id, funder);
        if (funder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Funder> result = funderService.partialUpdate(funder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funder.getId().toString())
        );
    }

    /**
     * {@code GET  /funders} : get all the funders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funders in body.
     */
    @GetMapping("/funders")
    public ResponseEntity<List<Funder>> getAllFunders(
        FunderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Funders by criteria: {}", criteria);

        Page<Funder> page = funderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funders/count} : count all the funders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/funders/count")
    public ResponseEntity<Long> countFunders(FunderCriteria criteria) {
        log.debug("REST request to count Funders by criteria: {}", criteria);
        return ResponseEntity.ok().body(funderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /funders/:id} : get the "id" funder.
     *
     * @param id the id of the funder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funders/{id}")
    public ResponseEntity<Funder> getFunder(@PathVariable Long id) {
        log.debug("REST request to get Funder : {}", id);
        Optional<Funder> funder = funderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funder);
    }

    /**
     * {@code DELETE  /funders/:id} : delete the "id" funder.
     *
     * @param id the id of the funder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funders/{id}")
    public ResponseEntity<Void> deleteFunder(@PathVariable Long id) {
        log.debug("REST request to delete Funder : {}", id);
        funderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
