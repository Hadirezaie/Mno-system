package af.mcit.mnosystem.service;

import af.mcit.mnosystem.domain.Imei;
import af.mcit.mnosystem.repository.ImeiRepository;
import af.mcit.mnosystem.service.criteria.ImeiCriteria;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing {@link Imei}.
 */
@Service
@Transactional
public class ImeiService {

    private final Logger log = LoggerFactory.getLogger(ImeiService.class);

    private final ImeiRepository imeiRepository;
    private final ImeiQueryService imeiQueryService;

    public ImeiService(ImeiRepository imeiRepository, ImeiQueryService imeiQueryService) {
        this.imeiRepository = imeiRepository;
        this.imeiQueryService = imeiQueryService;
    }

    /**
     * Save a imei.
     *
     * @param imei the entity to save.
     * @return the persisted entity.
     */
    public Imei save(Imei imei) {
        log.debug("Request to save Imei : {}", imei);

        ImeiCriteria imeiCriteria = new ImeiCriteria();
        imeiCriteria.setImeiNumber((StringFilter) new StringFilter().setEquals(imei.getImeiNumber()));
        List<Imei> imeiOpt = imeiQueryService.findByCriteria(imeiCriteria);
        Imei savedIme = new Imei();
        if (imeiOpt.isEmpty()) {
            savedIme = imeiRepository.save(imei);
        } else {
            Imei newImei = new Imei();
            imei.setId(imeiOpt.get(0).getId());
            imei.setImeiNumber(imeiOpt.get(0).getImeiNumber());
            imei.status(imei.getStatus());
            savedIme = imeiRepository.save(imei);
        }
        return savedIme;
    }

    /**
     * Update a imei.
     *
     * @param imei the entity to save.
     * @return the persisted entity.
     */
    public Imei update(Imei imei) {
        log.debug("Request to update Imei : {}", imei);
        return imeiRepository.save(imei);
    }

    /**
     * Partially update a imei.
     *
     * @param imei the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Imei> partialUpdate(Imei imei) {
        log.debug("Request to partially update Imei : {}", imei);

        return imeiRepository
            .findById(imei.getId())
            .map(existingImei -> {
                if (imei.getImeiNumber() != null) {
                    existingImei.setImeiNumber(imei.getImeiNumber());
                }
                if (imei.getStatus() != null) {
                    existingImei.setStatus(imei.getStatus());
                }

                return existingImei;
            })
            .map(imeiRepository::save);
    }

    /**
     * Get all the imeis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Imei> findAll(Pageable pageable) {
        log.debug("Request to get all Imeis");
        return imeiRepository.findAll(pageable);
    }

    /**
     * Get one imei by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Imei> findOne(Long id) {
        log.debug("Request to get Imei : {}", id);
        return imeiRepository.findById(id);
    }

    /**
     * Delete the imei by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Imei : {}", id);
        imeiRepository.deleteById(id);
    }
}
