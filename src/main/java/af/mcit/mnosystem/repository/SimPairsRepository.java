package af.mcit.mnosystem.repository;

import af.mcit.mnosystem.domain.SimPairs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimPairs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimPairsRepository extends JpaRepository<SimPairs, Long>, JpaSpecificationExecutor<SimPairs> {}
