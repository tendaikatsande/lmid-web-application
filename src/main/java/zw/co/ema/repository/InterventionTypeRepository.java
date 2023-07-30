package zw.co.ema.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.InterventionType;

/**
 * Spring Data JPA repository for the InterventionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterventionTypeRepository extends JpaRepository<InterventionType, Long>, JpaSpecificationExecutor<InterventionType> {}
