package uk.gov.defra.jncc.wff.resources.statics;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.IOException;
import java.io.StringWriter;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class SpatialHelper {

    public static final CoordinateReferenceSystem WSG84_SRS = DefaultGeographicCRS.WGS84;

    public static CoordinateReferenceSystem getOS_SRS() throws FactoryException {
        return CRS.decode("EPSG:27700");
    }

    public static String getGeoJSON(Geometry feature) throws IOException {
        GeometryJSON g = new GeometryJSON(14);
        StringWriter writer = new StringWriter();
        g.write(feature, writer);

        return writer.toString();
    }

    public static Geometry getGeometryFromWKT(String wkt, int srid) throws ParseException {
        WKTReader fromText = new WKTReader();
        Geometry geometry = fromText.read(wkt);
        geometry.setSRID(srid);
        return geometry;
    }
}
