/*
 */
package uk.gov.defra.jncc.wff.crud.repository;

import java.io.Serializable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;

/**
 *
 * @author Matt Debont
 */
public interface AttributedZoneRepository extends Repository<AttributedZone, Serializable>, QueryDslPredicateExecutor<AttributedZone> {
    
}
