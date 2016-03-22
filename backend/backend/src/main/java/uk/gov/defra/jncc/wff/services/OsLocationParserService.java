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
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

/**
 *
 * @author felix
 */
@Service
public class OsLocationParserService {
    

    public ArrayList<Location> GetMachingLocations(String jsonData, String query) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);
        ArrayList<Location> locations = new ArrayList<>();
 
        JsonNode resultsNode = root.path("results");
        if (resultsNode != null && resultsNode.isArray()) {
            resultsNode.forEach((JsonNode element) -> locations.add(GetLocationFromElement(element)));
        }
        
        //simplify result if exact match on postcode.
        if (locations.size() > 0 && IsExactPostcodeMatch(query, locations))
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
        location.centroid.x = x;
        location.centroid.y = y;
        
        return location;
    }
    
    private boolean IsExactPostcodeMatch(String query, ArrayList<Location> locations)
    {
        String cleanQuery = query.replaceAll("\\s","");
        String firstLocationId = locations.get(0).osId;
        
        return cleanQuery.compareToIgnoreCase(firstLocationId) == 0;
    }
    
}
