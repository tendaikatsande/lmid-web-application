package zw.co.ema.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.ema.domain.ApplicationUser;

/**
 * Spring Data JPA repository for the ApplicationUser entity.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
    default Optional<ApplicationUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ApplicationUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.province left join fetch applicationUser.district",
        countQuery = "select count(applicationUser) from ApplicationUser applicationUser"
    )
    Page<ApplicationUser> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.province left join fetch applicationUser.district"
    )
    List<ApplicationUser> findAllWithToOneRelationships();

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.province left join fetch applicationUser.district where applicationUser.id =:id"
    )
    Optional<ApplicationUser> findOneWithToOneRelationships(@Param("id") Long id);
}
