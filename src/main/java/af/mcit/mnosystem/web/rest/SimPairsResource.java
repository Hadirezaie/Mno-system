package af.mcit.mnosystem.web.rest;

import af.mcit.mnosystem.domain.SimPairs;
import af.mcit.mnosystem.repository.SimPairsRepository;
import af.mcit.mnosystem.service.SimPairsQueryService;
import af.mcit.mnosystem.service.SimPairsService;
import af.mcit.mnosystem.service.criteria.SimPairsCriteria;
import af.mcit.mnosystem.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link af.mcit.mnosystem.domain.SimPairs}.
 */
@RestController
@RequestMapping("/api")
public class SimPairsResource {

    private final Logger log = LoggerFactory.getLogger(SimPairsResource.class);

    private static final String ENTITY_NAME = "simPairs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimPairsService simPairsService;

    private final SimPairsRepository simPairsRepository;

    private final SimPairsQueryService simPairsQueryService;

    public SimPairsResource(
        SimPairsService simPairsService,
        SimPairsRepository simPairsRepository,
        SimPairsQueryService simPairsQueryService
    ) {
        this.simPairsService = simPairsService;
        this.simPairsRepository = simPairsRepository;
        this.simPairsQueryService = simPairsQueryService;
    }

    /**
     * {@code POST  /sim-pairs} : Create a new simPairs.
     *
     * @param simPairs the simPairs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new simPairs, or with status {@code 400 (Bad Request)} if the simPairs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sim-pairs")
    public ResponseEntity<SimPairs> createSimPairs(@RequestBody SimPairs simPairs) throws URISyntaxException {
        log.debug("REST request to save SimPairs : {}", simPairs);
        if (simPairs.getId() != null) {
            throw new BadRequestAlertException("A new simPairs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SimPairs result = simPairsService.save(simPairs);
        return ResponseEntity
            .created(new URI("/api/sim-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sim-pairs/:id} : Updates an existing simPairs.
     *
     * @param id the id of the simPairs to save.
     * @param simPairs the simPairs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simPairs,
     * or with status {@code 400 (Bad Request)} if the simPairs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the simPairs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sim-pairs/{id}")
    public ResponseEntity<SimPairs> updateSimPairs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SimPairs simPairs
    ) throws URISyntaxException {
        log.debug("REST request to update SimPairs : {}, {}", id, simPairs);
        if (simPairs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simPairs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simPairsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SimPairs result = simPairsService.update(simPairs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, simPairs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sim-pairs/:id} : Partial updates given fields of an existing simPairs, field will ignore if it is null
     *
     * @param id the id of the simPairs to save.
     * @param simPairs the simPairs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simPairs,
     * or with status {@code 400 (Bad Request)} if the simPairs is not valid,
     * or with status {@code 404 (Not Found)} if the simPairs is not found,
     * or with status {@code 500 (Internal Server Error)} if the simPairs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sim-pairs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimPairs> partialUpdateSimPairs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SimPairs simPairs
    ) throws URISyntaxException {
        log.debug("REST request to partial update SimPairs partially : {}, {}", id, simPairs);
        if (simPairs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simPairs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simPairsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimPairs> result = simPairsService.partialUpdate(simPairs);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, simPairs.getId().toString())
        );
    }

    /**
     * {@code GET  /sim-pairs} : get all the simPairs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of simPairs in body.
     */
    @GetMapping("/sim-pairs")
    public ResponseEntity<List<SimPairs>> getAllSimPairs(
        SimPairsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SimPairs by criteria: {}", criteria);
        Page<SimPairs> page = simPairsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sim-pairs/count} : count all the simPairs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sim-pairs/count")
    public ResponseEntity<Long> countSimPairs(SimPairsCriteria criteria) {
        log.debug("REST request to count SimPairs by criteria: {}", criteria);
        return ResponseEntity.ok().body(simPairsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sim-pairs/:id} : get the "id" simPairs.
     *
     * @param id the id of the simPairs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the simPairs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sim-pairs/{id}")
    public ResponseEntity<SimPairs> getSimPairs(@PathVariable Long id) {
        log.debug("REST request to get SimPairs : {}", id);
        Optional<SimPairs> simPairs = simPairsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(simPairs);
    }

    /**
     * {@code DELETE  /sim-pairs/:id} : delete the "id" simPairs.
     *
     * @param id the id of the simPairs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sim-pairs/{id}")
    public ResponseEntity<Void> deleteSimPairs(@PathVariable Long id) {
        log.debug("REST request to delete SimPairs : {}", id);
        simPairsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
