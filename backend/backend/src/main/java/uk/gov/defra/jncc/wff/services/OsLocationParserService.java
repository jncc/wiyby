/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import uk.gov.defra.jncc.wff.resources.Point;

/**
 *
 * @author felix
 */
@Service
public class OsLocationParserService {

    public List<Location> GetMachingLocations(String jsonData, String matchExpression) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);
        List<Location> locations = new ArrayList<>();
        
        JsonNode resultsNode = root.path("results");
        if (resultsNode != null && resultsNode.isArray()) {
            resultsNode.forEach((JsonNode element) -> locations.add(GetLocationFromElement(element)));
        }
        
        return locations;
    }

    private Location GetLocationFromElement(JsonNode element) {
        JsonNode gzEntry = element.path("GAZETTEER_ENTRY");
        Location location = new Location();
        
        location.name = gzEntry.path("NAME1").asText();
        double x = gzEntry.path("GEOMETRY_X").asDouble();
        double y = gzEntry.path("GEOMETRY_Y").asDouble();
        location.centroid = new Point(x,y);
        
        return location;
    }
    
    
}
