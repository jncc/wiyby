/*
 */
package uk.gov.defra.jncc.wff.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.utils.URIBuilder;
import org.geotools.GML;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author Matt Debont
 */
@Service
public class WFSQueryService {

    private static final String POLYGON_FILTER_X
            = "%20%3Cogc:Filter%20xmlns:ogc=%22http://www.opengis.net/ogc%22%3E%20%3Cogc:Intersects%3E%20%3Cogc:PropertyName%3Egeometry%3C/ogc:PropertyName%3E%20%3Cgml:Polygon%3E%20%3Cgml:outerBoundaryIs%3E%20%3Cgml:LinearRing%3E%20%3Cgml:coordinates%20cs=%22,%22%20ts=%22%20%22%3E471127.79444199602585286,310753.35005698999157175%20472253.23425156599842012,310769.89584677800303325%20472266.61112417001277208,309867.05561152601148933%20471140.96258526900783181,309850.50854017597157508%20471127.79444199602585286,310753.35005698999157175%3C/gml:coordinates%3E%20%3C/gml:LinearRing%3E%20%3C/gml:outerBoundaryIs%3E%20%3C/gml:Polygon%3E%20%3C/ogc:Intersects%3E%20%3C/ogc:Filter%3E";

    private static final String POLYGON_FILTER
            = "<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\">\n"
            + " <ogc:Intersects>\n"
            + "  <ogc:PropertyName>geometry</ogc:PropertyName>\n"
            + "  <gml:Polygon>\n"
            + "   <gml:outerBoundaryIs>\n"
            + "    <gml:LinearRing>\n"
            + "     <gml:coordinates cs=\",\" ts=\" \">%s</gml:coordinates>\n"
            + "    </gml:LinearRing>\n"
            + "   </gml:outerBoundaryIs>\n"
            + "  </gml:Polygon>\n"
            + " </ogc:Intersects>\n"
            + "</ogc:Filter>";
    
    public URI getUriForWfs(String baseWfsUrl, List<String> polygon) throws URISyntaxException
    {
        //url = url + "&FILTER=" + POLYGON_FILTER;
        URIBuilder builder = new URIBuilder(baseWfsUrl);
        builder.addParameter("FILTER", String.format(POLYGON_FILTER, polygon.stream().collect(Collectors.joining(" "))));
        return builder.build();
    }
    
    public SimpleFeatureCollection getGeometryForPolygon(String rawWfsResponse) throws IOException, SAXException, ParserConfigurationException {

        //GML gml = new GML(Version.WFS1_0);
        GML gml = new GML(GML.Version.GML2);
        SimpleFeatureCollection collection = gml.decodeFeatureCollection(new ByteArrayInputStream(rawWfsResponse.getBytes(StandardCharsets.UTF_8)));

        ReprojectingFeatureCollection rep = new ReprojectingFeatureCollection(collection, DefaultGeographicCRS.WGS84);

        return rep;
        
    }
}
