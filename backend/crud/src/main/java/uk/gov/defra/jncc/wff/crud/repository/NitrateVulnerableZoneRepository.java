package uk.gov.defra.jncc.wff.crud.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import uk.gov.defra.jncc.wff.crud.entity.spatial.NitrateVulnerableZone;

/**
 *
 * @author felix
 */
public interface NitrateVulnerableZoneRepository extends PagingAndSortingRepository<NitrateVulnerableZone, Serializable>, QueryDslPredicateExecutor<NitrateVulnerableZone>  {
    @Query("SELECT DISTINCT nvz.fid FROM NitrateVulnerableZone nvz ORDER BY nvz.date")
    public List<Integer> getAllNvxIds();
}
