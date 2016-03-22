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
import uk.gov.defra.jncc.wff.resources.statics.OSKeys;
import uk.gov.defra.jncc.wff.services.ReprojectionService;
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
    @Autowired ReprojectionService reprojector;
    
    @ResponseBody
    @RequestMapping(path = "/name/{query}", method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public ResponseEntity<uk.gov.defra.jncc.wff.resources.LocationResult> getLocationByName(
            @ApiParam(value = "The location query")
            @RequestParam(name = "query")  String query) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=";
        String queryUrl = apiUrl + URLEncoder.encode(query,"UTF-8") + "&key=" + OSKeys.OS_NAMES_KEY;
        String jsonResponse = "";
        
        uk.gov.defra.jncc.wff.resources.LocationResult result = new uk.gov.defra.jncc.wff.resources.LocationResult();
        result.setQuery(query);
        
        HttpStatus status = HttpStatus.OK;
        
        try 
        {
            jsonResponse = client.Get(queryUrl);
            ArrayList<Location> locations = osLocationParser.GetMachingLocations(jsonResponse, query);
            result.setLocations(locations);
        }
        catch (ClientProtocolException e)
        {
            result.addError("bad_request", "Invalid location query");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
    
    @ResponseBody
    @RequestMapping(path = "/bbox/{bbox}", method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public HttpEntity<uk.gov.defra.jncc.wff.resources.LocationResult> getLocationByBbox(
            @ApiParam(value = "The location query")
            @RequestParam(name = "bbox")  String bbox,
            @ApiParam(value = "The SRS of the bounding box")
            @RequestParam(name = "srs", required = false) int srs) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/places/v1/addresses/bbox?bbox=";
        
        if (srs == 0) srs = 4326;
        
        String osBbox = reprojector.ReprojectBbox(bbox, srs);
        
        throw new Exception("blarg");
        
    }
    
    
    
    
    

}
