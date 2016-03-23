/*
 */
package uk.gov.defra.jncc.wff.resources.statics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Matt Debont
 */
public class WFSHelper {


    public static List<String> getCoordPairsFromWKT(String wkt) throws ParseException, NoSuchAuthorityCodeException, FactoryException, TransformException {
        return WFSHelper.getCoordPairsFromWKT(wkt, SpatialHelper.WSG84_SRS, SpatialHelper.getOS_SRS());
    }

    public static List<String> getCoordPairsFromWKT(String wkt, CoordinateReferenceSystem s_srs, CoordinateReferenceSystem t_srs) throws ParseException, FactoryException, TransformException {
        WKTReader fromText = new WKTReader();
        Geometry polygon = fromText.read(wkt);

        MathTransform transform = CRS.findMathTransform(s_srs, t_srs);
        Geometry targetGeometry = JTS.transform(polygon, transform);

        Coordinate[] coords = targetGeometry.getCoordinates();

        return Arrays.stream(coords).map((c) -> String.format("%f,%f", c.x, c.y)).collect(Collectors.toList());
    }

    public static SimpleFeatureCollection transformCollection(SimpleFeatureCollection collection) {
//        CoordinateReferenceSystem ssrs = WFSHelper.DEFAULT_SOURCE_SRS;        
//        CoordinateReferenceSystem tsrs = CRS.decode(WFSHelper.DEFAULT_TARGET_SRS);
//                
//        SimpleFeatureCollection output = output.features().next()
//        
//        return collection;
        return null;
    }

}
