/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.topsat.crud.predicate.builders;

import com.mysema.query.types.expr.BooleanExpression;
import java.util.List;

/**
 *
 * @author felix
 */
public class PredicateBuilderHelper {
    public static BooleanExpression assemblePredicates(List<BooleanExpression> predicates) {
        BooleanExpression criteria = null;
        for (BooleanExpression p : predicates)
        {
            if (criteria == null) {
                criteria = p;
            } else {
                criteria = criteria.and(p);
            }
            
        }
        
        return criteria;
    }
}
