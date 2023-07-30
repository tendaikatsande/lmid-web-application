package zw.co.ema.web.rest;

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
import zw.co.ema.domain.InterventionFunder;
import zw.co.ema.repository.InterventionFunderRepository;
import zw.co.ema.service.InterventionFunderQueryService;
import zw.co.ema.service.InterventionFunderService;
import zw.co.ema.service.criteria.InterventionFunderCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.InterventionFunder}.
 */
@RestController
@RequestMapping("/api")
public class InterventionFunderResource {

    private final Logger log = LoggerFactory.getLogger(InterventionFunderResource.class);

    private static final String ENTITY_NAME = "interventionFunder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionFunderService interventionFunderService;

    private final InterventionFunderRepository interventionFunderRepository;

    private final InterventionFunderQueryService interventionFunderQueryService;

    public InterventionFunderResource(
        InterventionFunderService interventionFunderService,
        InterventionFunderRepository interventionFunderRepository,
        InterventionFunderQueryService interventionFunderQueryService
    ) {
        this.interventionFunderService = interventionFunderService;
        this.interventionFunderRepository = interventionFunderRepository;
        this.interventionFunderQueryService = interventionFunderQueryService;
    }

    /**
     * {@code POST  /intervention-funders} : Create a new interventionFunder.
     *
     * @param interventionFunder the interventionFunder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interventionFunder, or with status {@code 400 (Bad Request)} if the interventionFunder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intervention-funders")
    public ResponseEntity<InterventionFunder> createInterventionFunder(@RequestBody InterventionFunder interventionFunder)
        throws URISyntaxException {
        log.debug("REST request to save InterventionFunder : {}", interventionFunder);
        if (interventionFunder.getId() != null) {
            throw new BadRequestAlertException("A new interventionFunder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterventionFunder result = interventionFunderService.save(interventionFunder);
        return ResponseEntity
            .created(new URI("/api/intervention-funders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intervention-funders/:id} : Updates an existing interventionFunder.
     *
     * @param id the id of the interventionFunder to save.
     * @param interventionFunder the interventionFunder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionFunder,
     * or with status {@code 400 (Bad Request)} if the interventionFunder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interventionFunder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intervention-funders/{id}")
    public ResponseEntity<InterventionFunder> updateInterventionFunder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterventionFunder interventionFunder
    ) throws URISyntaxException {
        log.debug("REST request to update InterventionFunder : {}, {}", id, interventionFunder);
        if (interventionFunder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionFunder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionFunderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterventionFunder result = interventionFunderService.update(interventionFunder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionFunder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intervention-funders/:id} : Partial updates given fields of an existing interventionFunder, field will ignore if it is null
     *
     * @param id the id of the interventionFunder to save.
     * @param interventionFunder the interventionFunder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionFunder,
     * or with status {@code 400 (Bad Request)} if the interventionFunder is not valid,
     * or with status {@code 404 (Not Found)} if the interventionFunder is not found,
     * or with status {@code 500 (Internal Server Error)} if the interventionFunder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intervention-funders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterventionFunder> partialUpdateInterventionFunder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterventionFunder interventionFunder
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterventionFunder partially : {}, {}", id, interventionFunder);
        if (interventionFunder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionFunder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionFunderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterventionFunder> result = interventionFunderService.partialUpdate(interventionFunder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionFunder.getId().toString())
        );
    }

    /**
     * {@code GET  /intervention-funders} : get all the interventionFunders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventionFunders in body.
     */
    @GetMapping("/intervention-funders")
    public ResponseEntity<List<InterventionFunder>> getAllInterventionFunders(
        InterventionFunderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InterventionFunders by criteria: {}", criteria);

        Page<InterventionFunder> page = interventionFunderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /intervention-funders/count} : count all the interventionFunders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/intervention-funders/count")
    public ResponseEntity<Long> countInterventionFunders(InterventionFunderCriteria criteria) {
        log.debug("REST request to count InterventionFunders by criteria: {}", criteria);
        return ResponseEntity.ok().body(interventionFunderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /intervention-funders/:id} : get the "id" interventionFunder.
     *
     * @param id the id of the interventionFunder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interventionFunder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intervention-funders/{id}")
    public ResponseEntity<InterventionFunder> getInterventionFunder(@PathVariable Long id) {
        log.debug("REST request to get InterventionFunder : {}", id);
        Optional<InterventionFunder> interventionFunder = interventionFunderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interventionFunder);
    }

    /**
     * {@code DELETE  /intervention-funders/:id} : delete the "id" interventionFunder.
     *
     * @param id the id of the interventionFunder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intervention-funders/{id}")
    public ResponseEntity<Void> deleteInterventionFunder(@PathVariable Long id) {
        log.debug("REST request to delete InterventionFunder : {}", id);
        interventionFunderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
