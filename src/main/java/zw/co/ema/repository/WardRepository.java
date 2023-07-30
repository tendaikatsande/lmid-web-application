package zw.co.ema.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.Ward;

/**
 * Spring Data JPA repository for the Ward entity.
 */
@Repository
public interface WardRepository extends JpaRepository<Ward, Long>, JpaSpecificationExecutor<Ward> {
    default Optional<Ward> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ward> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ward> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select ward from Ward ward left join fetch ward.district", countQuery = "select count(ward) from Ward ward")
    Page<Ward> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ward from Ward ward left join fetch ward.district")
    List<Ward> findAllWithToOneRelationships();

    @Query("select ward from Ward ward left join fetch ward.district where ward.id =:id")
    Optional<Ward> findOneWithToOneRelationships(@Param("id") Long id);
}
