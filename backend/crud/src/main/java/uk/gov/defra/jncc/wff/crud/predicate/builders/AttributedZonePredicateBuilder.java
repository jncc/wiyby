package uk.gov.defra.jncc.wff.crud.predicate.builders;

import com.mysema.query.types.expr.BooleanExpression;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import uk.gov.defra.jncc.wff.crud.entity.spatial.QAttributedZone;
import uk.gov.defra.jncc.wff.crud.predicate.parameters.AttributedZoneParameters;

/**
 *
 * @author Matt Debont
 */
public class AttributedZonePredicateBuilder {

    public static BooleanExpression buildPredicates(AttributedZoneParameters params) throws ParseException {
        QAttributedZone az = QAttributedZone.attributedZone;

        List<BooleanExpression> predicates = new ArrayList<>();

        if (params.BoundingBoxWkt != null && !params.BoundingBoxWkt.isEmpty()) {
            WKTReader fromText = new WKTReader();
            Geometry boundingBox = fromText.read(params.BoundingBoxWkt);
            boundingBox.setSRID(4326);
            predicates.add(az.geom_wgs84.intersects(boundingBox));
        } else if (params.point_x != null && params.point_y != null && params.radius != null) {
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            Geometry point = geometryFactory.createPoint(new Coordinate(params.point_x, params.point_y));
            point.setSRID(4326);
            az.geom_wgs84.intersects(point.buffer(params.radius));
        }

        return PredicateBuilderHelper.assemblePredicates(predicates);
    }
}
