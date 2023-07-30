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
import zw.co.ema.domain.Ward;
import zw.co.ema.repository.WardRepository;
import zw.co.ema.service.WardQueryService;
import zw.co.ema.service.WardService;
import zw.co.ema.service.criteria.WardCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.Ward}.
 */
@RestController
@RequestMapping("/api")
public class WardResource {

    private final Logger log = LoggerFactory.getLogger(WardResource.class);

    private static final String ENTITY_NAME = "ward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WardService wardService;

    private final WardRepository wardRepository;

    private final WardQueryService wardQueryService;

    public WardResource(WardService wardService, WardRepository wardRepository, WardQueryService wardQueryService) {
        this.wardService = wardService;
        this.wardRepository = wardRepository;
        this.wardQueryService = wardQueryService;
    }

    /**
     * {@code POST  /wards} : Create a new ward.
     *
     * @param ward the ward to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ward, or with status {@code 400 (Bad Request)} if the ward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wards")
    public ResponseEntity<Ward> createWard(@Valid @RequestBody Ward ward) throws URISyntaxException {
        log.debug("REST request to save Ward : {}", ward);
        if (ward.getId() != null) {
            throw new BadRequestAlertException("A new ward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ward result = wardService.save(ward);
        return ResponseEntity
            .created(new URI("/api/wards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wards/:id} : Updates an existing ward.
     *
     * @param id the id of the ward to save.
     * @param ward the ward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ward,
     * or with status {@code 400 (Bad Request)} if the ward is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wards/{id}")
    public ResponseEntity<Ward> updateWard(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ward ward)
        throws URISyntaxException {
        log.debug("REST request to update Ward : {}, {}", id, ward);
        if (ward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ward result = wardService.update(ward);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ward.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wards/:id} : Partial updates given fields of an existing ward, field will ignore if it is null
     *
     * @param id the id of the ward to save.
     * @param ward the ward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ward,
     * or with status {@code 400 (Bad Request)} if the ward is not valid,
     * or with status {@code 404 (Not Found)} if the ward is not found,
     * or with status {@code 500 (Internal Server Error)} if the ward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ward> partialUpdateWard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ward ward
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ward partially : {}, {}", id, ward);
        if (ward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ward> result = wardService.partialUpdate(ward);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ward.getId().toString())
        );
    }

    /**
     * {@code GET  /wards} : get all the wards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wards in body.
     */
    @GetMapping("/wards")
    public ResponseEntity<List<Ward>> getAllWards(
        WardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Wards by criteria: {}", criteria);

        Page<Ward> page = wardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wards/count} : count all the wards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wards/count")
    public ResponseEntity<Long> countWards(WardCriteria criteria) {
        log.debug("REST request to count Wards by criteria: {}", criteria);
        return ResponseEntity.ok().body(wardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wards/:id} : get the "id" ward.
     *
     * @param id the id of the ward to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ward, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wards/{id}")
    public ResponseEntity<Ward> getWard(@PathVariable Long id) {
        log.debug("REST request to get Ward : {}", id);
        Optional<Ward> ward = wardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ward);
    }

    /**
     * {@code DELETE  /wards/:id} : delete the "id" ward.
     *
     * @param id the id of the ward to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wards/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        log.debug("REST request to delete Ward : {}", id);
        wardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
