/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import uk.gov.defra.jncc.wff.services.OsLocationParserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.jncc.wff.resources.Location;
import uk.gov.defra.jncc.wff.resources.LocationResult;
import uk.gov.defra.jncc.wff.services.RestClientService;

/**
 *
 * @author felix
 */
@RestController
@RequestMapping(path = "/location/search", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/location/search", description = "Location search api")
public class LocationSearch {
    @Autowired RestClientService client;
    @Autowired OsLocationParserService osLocationParser;
    
    private String apiKey = "W5gqiwe3T12gso3dnpGxABtfPyC0WJfb";    
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all Nitrate Vulnerable Zones", 
        response = String.class, 
        responseContainer = "Page")
    public HttpEntity<LocationResult> getLocations(
            @ApiParam(value = "The location query")
            @RequestParam(name = "query", required = false)  String query) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=";
        String queryUrl = apiUrl + URLEncoder.encode(query,"UTF-8") + "&key=" + apiKey;
        
        String jsonResponse = client.Get(queryUrl);
        
        ArrayList<Location> locations = osLocationParser.GetMachingLocations(jsonResponse, query);
        
        LocationResult result = new LocationResult(query, locations);
        

        return new HttpEntity<>(result);
    }
    

}
