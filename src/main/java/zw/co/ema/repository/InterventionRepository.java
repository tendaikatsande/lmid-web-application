package zw.co.ema.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.Intervention;

/**
 * Spring Data JPA repository for the Intervention entity.
 */
@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long>, JpaSpecificationExecutor<Intervention> {
    default Optional<Intervention> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Intervention> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Intervention> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select intervention from Intervention intervention left join fetch intervention.type left join fetch intervention.project left join fetch intervention.location left join fetch intervention.ward",
        countQuery = "select count(intervention) from Intervention intervention"
    )
    Page<Intervention> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select intervention from Intervention intervention left join fetch intervention.type left join fetch intervention.project left join fetch intervention.location left join fetch intervention.ward"
    )
    List<Intervention> findAllWithToOneRelationships();

    @Query(
        "select intervention from Intervention intervention left join fetch intervention.type left join fetch intervention.project left join fetch intervention.location left join fetch intervention.ward where intervention.id =:id"
    )
    Optional<Intervention> findOneWithToOneRelationships(@Param("id") Long id);
}
