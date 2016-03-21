/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.StringWriter;
import java.net.URI;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
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
import uk.gov.defra.jncc.wff.resources.statics.WFSHelper;
import uk.gov.defra.jncc.wff.services.RestClientService;
import uk.gov.defra.jncc.wff.services.WFSQueryService;

/**
 *
 * @author felix
 */
@RestController
@RequestMapping(path = "/wfs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/wfs", description = "WFS query controller")
public class WFSController {

    @Autowired
    WFSQueryService wfsQueryService;
    @Autowired RestClientService rcs;

    private static final String WFD_CATCHMENT_URL = "http://www.geostore.com/OGC/OGCInterface?version=1.1.0&INTERFACE=ENVIRONMENTWFS&LC=40000000000000000000000000000000000000000&SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=ea-wfs-eaieaew00160030&SRSNAME=EPSG:27700";

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves Water Catchment Areas for a given polygon",
            response = String.class)
    public ResponseEntity<String> getLocations(
            @ApiParam(value = "A WKT definition of the polygon to search for in WGS84")
            @RequestParam(name = "wkt", required = true) String wkt) throws Exception {
        
        URI wfsUri = wfsQueryService.getUriForWfs(WFD_CATCHMENT_URL, WFSHelper.getCoordPairsFromWKT(wkt));
        
        String rawWfsData = rcs.Get(wfsUri);
        
        SimpleFeatureCollection geoms = wfsQueryService.getGeometryForPolygon(rawWfsData);

        if (!geoms.isEmpty()) {
            FeatureJSON json = new FeatureJSON();
            StringWriter writer = new StringWriter();

            json.writeFeatureCollection(geoms, writer);
            //json.writeFeature(geoms.features().next(), writer);
            return new ResponseEntity<>(writer.toString(), HttpStatus.OK);
        }

        return new ResponseEntity<>("{type: \"FeatureCollection\", crs: {type: \"name\",properties: {name: \"EPSG:27700\"}},features: []}", HttpStatus.NO_CONTENT);
    }
}
