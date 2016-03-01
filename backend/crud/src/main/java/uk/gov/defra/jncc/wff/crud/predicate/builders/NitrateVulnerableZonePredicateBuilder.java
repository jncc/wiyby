/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.crud.predicate.builders;

import com.mysema.query.types.expr.BooleanExpression;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.ArrayList;
import java.util.List;
import uk.gov.defra.jncc.wff.crud.entity.spatial.QNitrateVulnerableZone;
import uk.gov.defra.jncc.wff.crud.predicate.parameters.NitrateVulnerableZoneParameters;

/**
 *
 * @author felix
 */
public class NitrateVulnerableZonePredicateBuilder {
       public static BooleanExpression buildPredicates(NitrateVulnerableZoneParameters params) throws ParseException {
            QNitrateVulnerableZone nvz = QNitrateVulnerableZone.nitrateVulnerableZone;

            List<BooleanExpression> predicates = new ArrayList<>();

            if (params.BoundingBoxWkt != null && !params.BoundingBoxWkt.isEmpty())
            {
                WKTReader fromText = new WKTReader();
                Geometry boundingBox = fromText.read(params.BoundingBoxWkt);
                boundingBox.setSRID(4326);
                predicates.add(nvz.geom.transform(4326).intersects(boundingBox));   
            }

            return PredicateBuilderHelper.assemblePredicates(predicates);
       }
}
