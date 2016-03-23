/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
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
import org.xml.sax.SAXException;
import uk.gov.defra.jncc.wff.resources.statics.WFSHelper;
import uk.gov.defra.jncc.wff.services.WFSQueryService;

/**
 *
 * @author Matt Debont
 */
@RestController
@RequestMapping(path = "/rest/wfs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/rest/wfs", description = "WFS query controller")
public class WFSController {

    @Autowired WFSQueryService wfsQueryService;

    // URL for the EA WFD WFS service
    private static final String WFD_CATCHMENT_URL = "http://www.geostore.com/OGC/OGCInterface?version=1.1.0&INTERFACE=ENVIRONMENTWFS&LC=40000000000000000000000000000000000000000&SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=ea-wfs-eaieaew00160030&SRSNAME=EPSG:27700";

    /**
     * Retrieves Water Catchment Areas for a given polygon defined in WKT 
     * (WGS84)
     * 
     * @param wkt WKT in WGS84 for a given query area
     * @return A Collection of geometries matched by the query area in GeoJSON
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves Water Catchment Areas for a given polygon",
            response = String.class)
    public ResponseEntity<String> getLocations(
            @ApiParam(value = "A WKT definition of the polygon to search for in WGS84")
            @RequestParam(name = "wkt", required = true) String wkt) {

        try {
            SimpleFeatureCollection geoms = wfsQueryService.getGeometryForPolygon(WFD_CATCHMENT_URL, WFSHelper.getCoordPairsFromWKT(wkt));

            if (!geoms.isEmpty()) {
                FeatureJSON json = new FeatureJSON();
                StringWriter writer = new StringWriter();

                json.writeFeatureCollection(geoms, writer);
                return new ResponseEntity<>(writer.toString(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("{type: \"FeatureCollection\", crs: {type: \"name\",properties: {name: \"EPSG:4326\"}},features: []}", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("{type: \"FeatureCollection\", crs: {type: \"name\",properties: {name: \"EPSG:4326\"}},features: []}", HttpStatus.NO_CONTENT);
    }
}
