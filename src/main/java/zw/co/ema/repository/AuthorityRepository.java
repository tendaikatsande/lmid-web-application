package zw.co.ema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.ema.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
