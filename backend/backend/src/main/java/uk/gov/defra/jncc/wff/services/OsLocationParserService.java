/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.services;

import uk.gov.defra.jncc.wff.resources.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;
import uk.gov.defra.jncc.wff.resources.statics.SpatialHelper;
import com.vividsolutions.jts.geom.Coordinate;

/**
 *
 * @author felix
 */
@Service
public class OsLocationParserService {
    
    private static int ENVELOPE_SCALE_FACTOR = 5;

    public ArrayList<Location> GetMachingLocations(String jsonData, String query) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);
        ArrayList<Location> locations = new ArrayList<>();
 
        JsonNode resultsNode = root.path("results");
        if (resultsNode != null && resultsNode.isArray()) {
            resultsNode.forEach((JsonNode element) -> locations.add(GetLocationFromElement(element)));
        }
        
        //simplify result if exact match on postcode.
        if (query != "" && locations.size() > 0 && IsExactPostcodeMatch(query, locations))
        {
           ArrayList<Location> singleResult = new ArrayList<>();
           singleResult.add(locations.get(0));
           
           return singleResult;
        }

        return locations;
        
    }

    private Location GetLocationFromElement(JsonNode element) {
        JsonNode gzEntry = element.path("GAZETTEER_ENTRY");
        Location location = new Location();
        
        String name = gzEntry.path("NAME1").asText();
        String place = gzEntry.path("POPULATED_PLACE").asText();
        String borough = gzEntry.path("DISTRICT_BOROUGH").asText(); 
        
        location.osId = gzEntry.path("ID").asText();
        location.name = name + " " + place + " " + borough;
        double x = gzEntry.path("GEOMETRY_X").asDouble();
        double y = gzEntry.path("GEOMETRY_Y").asDouble();
        location.wktCentroid = GetCentroidFromOSPoint(x, y);
        location.wktBbox = GetBboxFromOSPoint(x, y);
        
        return location;
    }
    
    private boolean IsExactPostcodeMatch(String query, ArrayList<Location> locations)
    {
        String cleanQuery = query.replaceAll("\\s","");
        String firstLocationId = locations.get(0).osId;
        
        return cleanQuery.compareToIgnoreCase(firstLocationId) == 0;
    }
    
    public String GetCentroidFromOSPoint(double x, double y) {
        Coordinate c = new Coordinate(x ,y );
        
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 27700);
        Geometry sourceGeom = factory.createPoint(c);
        
        return GetWKTFromOSGeometry(sourceGeom);
    }
    
    public String GetBboxFromOSPoint(double x, double y) {
        Envelope env = new Envelope(x - ENVELOPE_SCALE_FACTOR, x + ENVELOPE_SCALE_FACTOR, y - ENVELOPE_SCALE_FACTOR, y + ENVELOPE_SCALE_FACTOR);

        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 27700);
        Geometry sourceGeom = factory.toGeometry(env);
        
        return GetWKTFromOSGeometry(sourceGeom);
    }
    
    private String GetWKTFromOSGeometry(Geometry sourceGeom) {
        Geometry targetGeom;
            
        try {
            MathTransform transform = CRS.findMathTransform(SpatialHelper.getOS_SRS(), SpatialHelper.WSG84_SRS);
            targetGeom = JTS.transform(sourceGeom, transform);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
           throw new RuntimeException(ex.getMessage());
        }
        
        WKTWriter toText = new WKTWriter();
        return toText.write(targetGeom);
    }
    
}
