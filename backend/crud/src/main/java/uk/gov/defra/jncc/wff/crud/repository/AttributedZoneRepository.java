/*
 */
package uk.gov.defra.jncc.wff.crud.repository;

import com.vividsolutions.jts.geom.Geometry;
import java.io.Serializable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;

/**
 *
 * @author Matt Debont
 */
public interface AttributedZoneRepository extends Repository<AttributedZone, Serializable>, QueryDslPredicateExecutor<AttributedZone> {

    @Query(value = "SELECT ST_AsGeoJSON(:wkt)", nativeQuery = true)
    public String getGeoJSON(@Param("wkt") String wkt);

    @Query(value = "SELECT ST_AsGeoJSON(:geom)", nativeQuery = true)
    public String getGeoJSON(@Param("geom") Geometry geom);
}
