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
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.jncc.wff.resources.Location;
import uk.gov.defra.jncc.wff.resources.LocationResult;
import uk.gov.defra.jncc.wff.resources.statics.OSKeys;
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
    
    @ResponseBody
    @RequestMapping(path = "/name/{query}", method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public ResponseEntity<LocationResult> getLocationByName(
            @ApiParam(value = "The location query")
            @PathVariable("query")  String query) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=";
        String queryUrl = apiUrl + URLEncoder.encode(query,"UTF-8") + "&key=" + OSKeys.OS_NAMES_KEY;
        String jsonResponse = "";
        
        try 
        {
            jsonResponse = client.Get(queryUrl);
        }
        catch (ClientProtocolException e)
        {
            return new ResponseEntity("Invalid search term", HttpStatus.BAD_REQUEST);
        }

        ArrayList<Location> locations = osLocationParser.GetMachingLocations(jsonResponse, query);
        
        LocationResult result = new LocationResult(query, locations);
       
        return new ResponseEntity(result, HttpStatus.OK);
    }
    
    @ResponseBody
    @RequestMapping(path = "/bbox", method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public HttpEntity<LocationResult> getLocationByBbox(
            @ApiParam(value = "The location query")
            @RequestParam(name = "bbox", required = false)  String bbox) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/places/v1/addresses/bbox?bbox=";
        
        throw new Exception("blarg");
        
    }
    
    
    

}
