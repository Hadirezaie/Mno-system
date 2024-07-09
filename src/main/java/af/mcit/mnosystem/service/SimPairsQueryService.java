package af.mcit.mnosystem.service;

import af.mcit.mnosystem.domain.*; // for static metamodels
import af.mcit.mnosystem.domain.SimPairs;
import af.mcit.mnosystem.repository.SimPairsRepository;
import af.mcit.mnosystem.service.criteria.SimPairsCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SimPairs} entities in the database.
 * The main input is a {@link SimPairsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SimPairs} or a {@link Page} of {@link SimPairs} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimPairsQueryService extends QueryService<SimPairs> {

    private final Logger log = LoggerFactory.getLogger(SimPairsQueryService.class);

    private final SimPairsRepository simPairsRepository;

    public SimPairsQueryService(SimPairsRepository simPairsRepository) {
        this.simPairsRepository = simPairsRepository;
    }

    /**
     * Return a {@link List} of {@link SimPairs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SimPairs> findByCriteria(SimPairsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SimPairs> specification = createSpecification(criteria);
        return simPairsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SimPairs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimPairs> findByCriteria(SimPairsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SimPairs> specification = createSpecification(criteria);
        return simPairsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimPairsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SimPairs> specification = createSpecification(criteria);
        return simPairsRepository.count(specification);
    }

    /**
     * Function to convert {@link SimPairsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SimPairs> createSpecification(SimPairsCriteria criteria) {
        Specification<SimPairs> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SimPairs_.id));
            }
            if (criteria.getMsisdn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMsisdn(), SimPairs_.msisdn));
            }
            if (criteria.getImsi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImsi(), SimPairs_.imsi));
            }
            if (criteria.getSent() != null) {
                specification = specification.and(buildSpecification(criteria.getSent(), SimPairs_.sent));
            }
            if (criteria.getImeiId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImeiId(), root -> root.join(SimPairs_.imei, JoinType.LEFT).get(Imei_.id))
                    );
            }
        }
        return specification;
    }
}
