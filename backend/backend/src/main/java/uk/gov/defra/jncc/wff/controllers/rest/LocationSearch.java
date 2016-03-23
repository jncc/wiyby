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
import uk.gov.defra.jncc.wff.services.ReprojectionService;
import uk.gov.defra.jncc.wff.services.RestClientService;

/**
 *
 * @author felix
 */
@RestController
@RequestMapping(path = "/rest/location/search", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/rest/location/search", description = "Location search api")
public class LocationSearch {
    @Autowired RestClientService client;
    @Autowired OsLocationParserService osLocationParser;
    @Autowired ReprojectionService reprojector;
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public ResponseEntity<LocationResult> getLocation(
            @ApiParam(value = "The location query")
            @RequestParam(name = "query", required = false)  String query,
            @RequestParam(name = "bbox", required = false)  String bbox,
            @RequestParam(name = "srs", required = false)  Integer srs) throws Exception {
        LocationResult result = new LocationResult();
        
        HttpStatus status = HttpStatus.OK;
        
        if ((query == null && bbox == null) || (query != null && bbox != null)) {
            result.addError("invalid_search_request", "Either a bounding box or a search query must be specified");
            status = HttpStatus.BAD_REQUEST;
        } else if (query != null) {
            result.setQuery(query);
            try 
            {
                result.setLocations(getLocationsByName(query));
            }
            catch (ClientProtocolException e)
            {
                result.addError("bad_request", "Invalid location query");
                status = HttpStatus.BAD_REQUEST;
            }

        } else {
            result.setQuery(bbox);
            if (srs == null || srs == 0) srs = 4326;
            result.setLocations(getLocationsByBbox(bbox,srs));
        }
        
        return new ResponseEntity(result, status);
    }
    
    private List<Location> getLocationsByName(String query) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=";
        String queryUrl = apiUrl + URLEncoder.encode(query,"UTF-8") + "&key=" + OSKeys.OS_NAMES_KEY;
     
        String jsonResponse = client.Get(queryUrl);
        return osLocationParser.GetMachingLocations(jsonResponse, query);
    } 
    
    private List<Location> getLocationsByBbox(
            @ApiParam(value = "The location query")
            @RequestParam(name = "bbox")  String bbox,
            @ApiParam(value = "The SRS of the bounding box")
            @RequestParam(name = "srs", required = false) Integer srs) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/places/v1/addresses/bbox?bbox=";
        
        String osBbox = reprojector.ReprojectBbox(bbox, srs);
        
        throw new Exception("blarg");
        
    }
      

}
