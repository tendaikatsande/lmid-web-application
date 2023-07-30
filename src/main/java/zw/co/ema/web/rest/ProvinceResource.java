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
import zw.co.ema.domain.Province;
import zw.co.ema.repository.ProvinceRepository;
import zw.co.ema.service.ProvinceQueryService;
import zw.co.ema.service.ProvinceService;
import zw.co.ema.service.criteria.ProvinceCriteria;
import zw.co.ema.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.ema.domain.Province}.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

    private static final String ENTITY_NAME = "province";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinceService provinceService;

    private final ProvinceRepository provinceRepository;

    private final ProvinceQueryService provinceQueryService;

    public ProvinceResource(
        ProvinceService provinceService,
        ProvinceRepository provinceRepository,
        ProvinceQueryService provinceQueryService
    ) {
        this.provinceService = provinceService;
        this.provinceRepository = provinceRepository;
        this.provinceQueryService = provinceQueryService;
    }

    /**
     * {@code POST  /provinces} : Create a new province.
     *
     * @param province the province to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new province, or with status {@code 400 (Bad Request)} if the province has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provinces")
    public ResponseEntity<Province> createProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to save Province : {}", province);
        if (province.getId() != null) {
            throw new BadRequestAlertException("A new province cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Province result = provinceService.save(province);
        return ResponseEntity
            .created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provinces/:id} : Updates an existing province.
     *
     * @param id the id of the province to save.
     * @param province the province to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated province,
     * or with status {@code 400 (Bad Request)} if the province is not valid,
     * or with status {@code 500 (Internal Server Error)} if the province couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provinces/{id}")
    public ResponseEntity<Province> updateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Province province
    ) throws URISyntaxException {
        log.debug("REST request to update Province : {}, {}", id, province);
        if (province.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, province.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Province result = provinceService.update(province);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, province.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /provinces/:id} : Partial updates given fields of an existing province, field will ignore if it is null
     *
     * @param id the id of the province to save.
     * @param province the province to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated province,
     * or with status {@code 400 (Bad Request)} if the province is not valid,
     * or with status {@code 404 (Not Found)} if the province is not found,
     * or with status {@code 500 (Internal Server Error)} if the province couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/provinces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Province> partialUpdateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Province province
    ) throws URISyntaxException {
        log.debug("REST request to partial update Province partially : {}, {}", id, province);
        if (province.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, province.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Province> result = provinceService.partialUpdate(province);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, province.getId().toString())
        );
    }

    /**
     * {@code GET  /provinces} : get all the provinces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provinces in body.
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces(
        ProvinceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Provinces by criteria: {}", criteria);

        Page<Province> page = provinceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provinces/count} : count all the provinces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/provinces/count")
    public ResponseEntity<Long> countProvinces(ProvinceCriteria criteria) {
        log.debug("REST request to count Provinces by criteria: {}", criteria);
        return ResponseEntity.ok().body(provinceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /provinces/:id} : get the "id" province.
     *
     * @param id the id of the province to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the province, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provinces/{id}")
    public ResponseEntity<Province> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Optional<Province> province = provinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(province);
    }

    /**
     * {@code DELETE  /provinces/:id} : delete the "id" province.
     *
     * @param id the id of the province to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
