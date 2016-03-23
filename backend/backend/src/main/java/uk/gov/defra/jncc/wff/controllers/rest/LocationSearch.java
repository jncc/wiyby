/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.ParseException;
import uk.gov.defra.jncc.wff.services.OsLocationParserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URLEncoder;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.jncc.wff.resources.Location;
import uk.gov.defra.jncc.wff.resources.LocationResult;
import uk.gov.defra.jncc.wff.resources.statics.OSKeys;
import uk.gov.defra.jncc.wff.services.EnvelopeGeneratorService;
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
    @Autowired EnvelopeGeneratorService envGenerator;
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Location search", 
        response = String.class, 
        responseContainer = "Page")
    public ResponseEntity<LocationResult> getLocation(
            @ApiParam(value = "The location query")
            @RequestParam(name = "query", required = false)  String query,
            @RequestParam(name = "polygon", required = false)  String polygon,
            @RequestParam(name = "type", required = false)  String geosearchType) throws Exception {
        LocationResult result = new LocationResult();
        
        HttpStatus status = HttpStatus.OK;
        
        if ((query == null && polygon == null) || (query != null && polygon != null)) {
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
            result.setQuery(polygon);
            if (geosearchType == null) geosearchType = "centroid";
            
            try 
            {
                result.setLocations(getLocationsByPolygon(polygon, geosearchType));
            }
            catch (ClientProtocolException e)
            {
                result.addError("bad_request", "Invalid location query");
                status = HttpStatus.BAD_REQUEST;
            }
        }
        
        return new ResponseEntity(result, status);
    }
    
    private List<Location> getLocationsByName(String query) throws Exception {   
        String apiUrl = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=";
        String queryUrl = apiUrl + URLEncoder.encode(query,"UTF-8") + "&key=" + OSKeys.OS_NAMES_KEY;
     
        String jsonResponse = client.Get(queryUrl);
        return osLocationParser.GetMachingLocations(jsonResponse, query);
    } 
    
    private List<Location> getLocationsByPolygon(String polygonWkt, String geosearchType) throws Exception {   
        
        if ("envelope".equals(geosearchType)) {
            return getLocationsByEnvelope(polygonWkt);
        } else if ("centroid".equals(geosearchType)) {
            return getLocationsByCentroid(polygonWkt);
        } else {
            throw new ClientProtocolException("Bad Request");
        }
    }
    
    private List<Location> getLocationsByEnvelope(String polygonWkt) throws Exception {
        String apiUrl = "https://api.ordnancesurvey.co.uk/places/v1/addresses/bbox?bbox=";
        
        Envelope env = envGenerator.GetOSEvelopeFromPolygon(polygonWkt);
        
        String minX = String.valueOf(env.getMinX());
        String minY = String.valueOf(env.getMinY());
        String maxX = String.valueOf(env.getMaxX());
        String maxY = String.valueOf(env.getMaxY());
        
        String queryUrl = apiUrl + minX + "," + minY + "," + maxX + "," + maxY + "&key=" + OSKeys.OS_NAMES_KEY;
        
        String jsonResponse = client.Get(queryUrl);
        return osLocationParser.GetMachingLocations(jsonResponse, "");
    }

    private List<Location> getLocationsByCentroid(String polygonWkt) throws Exception {
        String apiUrl = "https://api.ordnancesurvey.co.uk/places/v1/addresses/radius?point=";
        
        Coordinate c = envGenerator.GetOSCentroidFromPolygon(polygonWkt);
        
        String x = String.valueOf(c.x);
        String y = String.valueOf(c.y);
        
        String queryUrl = apiUrl + x + "," + y + "&radius=" + 100 + "&key=" + OSKeys.OS_NAMES_KEY;
        
        String jsonResponse = client.Get(queryUrl);
        return osLocationParser.GetMachingLocations(jsonResponse, "");
    }
}
