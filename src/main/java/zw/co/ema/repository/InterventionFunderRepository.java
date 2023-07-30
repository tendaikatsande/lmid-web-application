package zw.co.ema.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.InterventionFunder;

/**
 * Spring Data JPA repository for the InterventionFunder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterventionFunderRepository
    extends JpaRepository<InterventionFunder, Long>, JpaSpecificationExecutor<InterventionFunder> {}
