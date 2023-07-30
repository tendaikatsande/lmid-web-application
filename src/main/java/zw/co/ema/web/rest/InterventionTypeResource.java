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
import zw.co.ema.domain.InterventionType;
import zw.co.ema.repository.InterventionTypeRepository;
import zw.co.ema.service.InterventionTypeQueryService;
import zw.co.ema.service.InterventionTypeService;
import zw.co.ema.service.criteria.InterventionTypeCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.InterventionType}.
 */
@RestController
@RequestMapping("/api")
public class InterventionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InterventionTypeResource.class);

    private static final String ENTITY_NAME = "interventionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionTypeService interventionTypeService;

    private final InterventionTypeRepository interventionTypeRepository;

    private final InterventionTypeQueryService interventionTypeQueryService;

    public InterventionTypeResource(
        InterventionTypeService interventionTypeService,
        InterventionTypeRepository interventionTypeRepository,
        InterventionTypeQueryService interventionTypeQueryService
    ) {
        this.interventionTypeService = interventionTypeService;
        this.interventionTypeRepository = interventionTypeRepository;
        this.interventionTypeQueryService = interventionTypeQueryService;
    }

    /**
     * {@code POST  /intervention-types} : Create a new interventionType.
     *
     * @param interventionType the interventionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interventionType, or with status {@code 400 (Bad Request)} if the interventionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intervention-types")
    public ResponseEntity<InterventionType> createInterventionType(@Valid @RequestBody InterventionType interventionType)
        throws URISyntaxException {
        log.debug("REST request to save InterventionType : {}", interventionType);
        if (interventionType.getId() != null) {
            throw new BadRequestAlertException("A new interventionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterventionType result = interventionTypeService.save(interventionType);
        return ResponseEntity
            .created(new URI("/api/intervention-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intervention-types/:id} : Updates an existing interventionType.
     *
     * @param id the id of the interventionType to save.
     * @param interventionType the interventionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionType,
     * or with status {@code 400 (Bad Request)} if the interventionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interventionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intervention-types/{id}")
    public ResponseEntity<InterventionType> updateInterventionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterventionType interventionType
    ) throws URISyntaxException {
        log.debug("REST request to update InterventionType : {}, {}", id, interventionType);
        if (interventionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterventionType result = interventionTypeService.update(interventionType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intervention-types/:id} : Partial updates given fields of an existing interventionType, field will ignore if it is null
     *
     * @param id the id of the interventionType to save.
     * @param interventionType the interventionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionType,
     * or with status {@code 400 (Bad Request)} if the interventionType is not valid,
     * or with status {@code 404 (Not Found)} if the interventionType is not found,
     * or with status {@code 500 (Internal Server Error)} if the interventionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intervention-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterventionType> partialUpdateInterventionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterventionType interventionType
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterventionType partially : {}, {}", id, interventionType);
        if (interventionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterventionType> result = interventionTypeService.partialUpdate(interventionType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionType.getId().toString())
        );
    }

    /**
     * {@code GET  /intervention-types} : get all the interventionTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventionTypes in body.
     */
    @GetMapping("/intervention-types")
    public ResponseEntity<List<InterventionType>> getAllInterventionTypes(
        InterventionTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InterventionTypes by criteria: {}", criteria);

        Page<InterventionType> page = interventionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /intervention-types/count} : count all the interventionTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/intervention-types/count")
    public ResponseEntity<Long> countInterventionTypes(InterventionTypeCriteria criteria) {
        log.debug("REST request to count InterventionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(interventionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /intervention-types/:id} : get the "id" interventionType.
     *
     * @param id the id of the interventionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interventionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intervention-types/{id}")
    public ResponseEntity<InterventionType> getInterventionType(@PathVariable Long id) {
        log.debug("REST request to get InterventionType : {}", id);
        Optional<InterventionType> interventionType = interventionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interventionType);
    }

    /**
     * {@code DELETE  /intervention-types/:id} : delete the "id" interventionType.
     *
     * @param id the id of the interventionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intervention-types/{id}")
    public ResponseEntity<Void> deleteInterventionType(@PathVariable Long id) {
        log.debug("REST request to delete InterventionType : {}", id);
        interventionTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
