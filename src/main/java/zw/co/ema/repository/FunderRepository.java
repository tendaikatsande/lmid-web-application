package zw.co.ema.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.Funder;

/**
 * Spring Data JPA repository for the Funder entity.
 */
@Repository
public interface FunderRepository extends JpaRepository<Funder, Long>, JpaSpecificationExecutor<Funder> {
    default Optional<Funder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Funder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Funder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select funder from Funder funder left join fetch funder.sector", countQuery = "select count(funder) from Funder funder")
    Page<Funder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select funder from Funder funder left join fetch funder.sector")
    List<Funder> findAllWithToOneRelationships();

    @Query("select funder from Funder funder left join fetch funder.sector where funder.id =:id")
    Optional<Funder> findOneWithToOneRelationships(@Param("id") Long id);
}
