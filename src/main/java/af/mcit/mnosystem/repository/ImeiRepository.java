package af.mcit.mnosystem.repository;

import af.mcit.mnosystem.domain.Imei;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Imei entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImeiRepository extends JpaRepository<Imei, Long>, JpaSpecificationExecutor<Imei> {}
