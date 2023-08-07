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
import zw.co.ema.domain.Intervention;
import zw.co.ema.repository.InterventionRepository;
import zw.co.ema.service.InterventionQueryService;
import zw.co.ema.service.InterventionService;
import zw.co.ema.service.criteria.InterventionCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.Intervention}.
 */
@RestController
@RequestMapping("/api")
public class InterventionResource {

    private final Logger log = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionService interventionService;

    private final InterventionRepository interventionRepository;

    private final InterventionQueryService interventionQueryService;

    public InterventionResource(
        InterventionService interventionService,
        InterventionRepository interventionRepository,
        InterventionQueryService interventionQueryService
    ) {
        this.interventionService = interventionService;
        this.interventionRepository = interventionRepository;
        this.interventionQueryService = interventionQueryService;
    }

    /**
     * {@code POST  /interventions} : Create a new intervention.
     *
     * @param intervention the intervention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intervention, or with status {@code 400 (Bad Request)} if the intervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interventions")
    public ResponseEntity<Intervention> createIntervention(@Valid @RequestBody Intervention intervention) throws URISyntaxException {
        log.debug("REST request to save Intervention : {}", intervention);
        if (intervention.getId() != null) {
            throw new BadRequestAlertException("A new intervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Intervention result = interventionService.save(intervention);
        return ResponseEntity
            .created(new URI("/api/interventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interventions/:id} : Updates an existing intervention.
     *
     * @param id the id of the intervention to save.
     * @param intervention the intervention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervention,
     * or with status {@code 400 (Bad Request)} if the intervention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intervention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interventions/{id}")
    public ResponseEntity<Intervention> updateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Intervention intervention
    ) throws URISyntaxException {
        log.debug("REST request to update Intervention : {}, {}", id, intervention);
        if (intervention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Intervention result = interventionService.update(intervention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interventions/:id} : Partial updates given fields of an existing intervention, field will ignore if it is null
     *
     * @param id the id of the intervention to save.
     * @param intervention the intervention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervention,
     * or with status {@code 400 (Bad Request)} if the intervention is not valid,
     * or with status {@code 404 (Not Found)} if the intervention is not found,
     * or with status {@code 500 (Internal Server Error)} if the intervention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interventions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Intervention> partialUpdateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Intervention intervention
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intervention partially : {}, {}", id, intervention);
        if (intervention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Intervention> result = interventionService.partialUpdate(intervention);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervention.getId().toString())
        );
    }

    /**
     * {@code GET  /interventions} : get all the interventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventions in body.
     */
    @GetMapping("/interventions")
    public ResponseEntity<List<Intervention>> getAllInterventions(
        InterventionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Interventions by criteria: {}", criteria);

        Page<Intervention> page = interventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interventions/count} : count all the interventions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interventions/count")
    public ResponseEntity<Long> countInterventions(InterventionCriteria criteria) {
        log.debug("REST request to count Interventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(interventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interventions/:id} : get the "id" intervention.
     *
     * @param id the id of the intervention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intervention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interventions/{id}")
    public ResponseEntity<Intervention> getIntervention(@PathVariable Long id) {
        log.debug("REST request to get Intervention : {}", id);
        Optional<Intervention> intervention = interventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intervention);
    }

    /**
     * {@code DELETE  /interventions/:id} : delete the "id" intervention.
     *
     * @param id the id of the intervention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interventions/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        log.debug("REST request to delete Intervention : {}", id);
        interventionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
