package af.mcit.mnosystem.service;

import af.mcit.mnosystem.domain.SimPairs;
import af.mcit.mnosystem.repository.SimPairsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SimPairs}.
 */
@Service
@Transactional
public class SimPairsService {

    private final Logger log = LoggerFactory.getLogger(SimPairsService.class);

    private final SimPairsRepository simPairsRepository;

    public SimPairsService(SimPairsRepository simPairsRepository) {
        this.simPairsRepository = simPairsRepository;
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
}
