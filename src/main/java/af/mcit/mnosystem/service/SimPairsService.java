package af.mcit.mnosystem.service;

import af.mcit.mnosystem.domain.SimPairs;
import af.mcit.mnosystem.repository.SimPairsRepository;
import af.mcit.mnosystem.service.criteria.SimPairsCriteria;
import af.mcit.mnosystem.service.dto.SimPairsDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;

/**
 * Service Implementation for managing {@link SimPairs}.
 */
@Service
@Transactional
public class SimPairsService {

    private final Logger log = LoggerFactory.getLogger(SimPairsService.class);

    private final SimPairsRepository simPairsRepository;
    private final SimPairsQueryService simPairsQueryService;
    private final CIRTokenService cirTokenService;

    public SimPairsService(
        SimPairsRepository simPairsRepository,
        SimPairsQueryService simPairsQueryService,
        CIRTokenService cirTokenService
    ) {
        this.simPairsRepository = simPairsRepository;
        this.simPairsQueryService = simPairsQueryService;
        this.cirTokenService = cirTokenService;
    }

    /**
     * Save a simPairs.
     *
     * @param simPairs the entity to save.
     * @return the persisted entity.
     */
    public SimPairs save(SimPairs simPairs) {
        log.debug("Request to save SimPairs : {}", simPairs);
        return simPairsRepository.save(simPairs);
    }

    /**
     * Update a simPairs.
     *
     * @param simPairs the entity to save.
     * @return the persisted entity.
     */
    public SimPairs update(SimPairs simPairs) {
        log.debug("Request to update SimPairs : {}", simPairs);
        return simPairsRepository.save(simPairs);
    }

    /**
     * Partially update a simPairs.
     *
     * @param simPairs the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SimPairs> partialUpdate(SimPairs simPairs) {
        log.debug("Request to partially update SimPairs : {}", simPairs);

        return simPairsRepository
            .findById(simPairs.getId())
            .map(existingSimPairs -> {
                if (simPairs.getMsisdn() != null) {
                    existingSimPairs.setMsisdn(simPairs.getMsisdn());
                }
                if (simPairs.getImsi() != null) {
                    existingSimPairs.setImsi(simPairs.getImsi());
                }
                if (simPairs.getImeiNumber() != null) {
                    existingSimPairs.setImeiNumber(simPairs.getImeiNumber());
                }
                if (simPairs.getSent() != null) {
                    existingSimPairs.setSent(simPairs.getSent());
                }

                return existingSimPairs;
            })
            .map(simPairsRepository::save);
    }

    /**
     * Get all the simPairs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SimPairs> findAll(Pageable pageable) {
        log.debug("Request to get all SimPairs");
        return simPairsRepository.findAll(pageable);
    }

    /**
     * Get one simPairs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SimPairs> findOne(Long id) {
        log.debug("Request to get SimPairs : {}", id);
        return simPairsRepository.findById(id);
    }

    /**
     * Delete the simPairs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SimPairs : {}", id);
        simPairsRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 1000)
    protected void sendPairedSimsToCIR() {
        SimPairsCriteria simPairsCriteria = new SimPairsCriteria();
        simPairsCriteria.setSent((BooleanFilter) new BooleanFilter().setEquals(false));
        List<SimPairs> simPairs = simPairsQueryService.findByCriteria(simPairsCriteria);
        simPairs.forEach(pairs -> {
            try {
                String token = cirTokenService.getToken();
                String url = "http://localhost:8080/api/sim-pair";
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<SimPairs> request = new HttpEntity<SimPairs>(pairs, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                log.info("Call Sim Pairs API: {}", response.getBody());

                pairs.setSent(true);

                SimPairs result = simPairsRepository.save(pairs);
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }
        });
    }
}
