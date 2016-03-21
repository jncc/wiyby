package uk.gov.defra.jncc.wff.controllers.rest;

import com.mysema.query.types.expr.BooleanExpression;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;
import uk.gov.defra.jncc.wff.crud.predicate.builders.AttributedZonePredicateBuilder;
import uk.gov.defra.jncc.wff.crud.predicate.parameters.AttributedZoneParameters;
import uk.gov.defra.jncc.wff.crud.repository.AttributedZoneRepository;
import uk.gov.defra.jncc.wff.resources.Report;
import uk.gov.defra.jncc.wff.resources.assemblers.ReportAssembler;

/**
 *
 * @author Matt Debont
 */
@RestController
@RequestMapping(path = "/rest/report", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/rest/report", description = "Generates report objects for a given area for use by a frontend application")
public class ReportController {

    @Autowired
    AttributedZoneRepository attributedZoneRepository;

    @ResponseBody
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Generates a report object for a given area, either defined as a WKT polygon or a point and radius in WGS84 (EPSG:4326)",
            response = Report.class)
    public ResponseEntity<Report> search(
            @ApiParam(value = "A WKT bounding box defined in EPSG:4326")
            @RequestParam(name = "wkt", required = false) String wkt,
            @ApiParam(value = "A Point defined as x,y defined in EPSG:4326")
            @RequestParam(value = "point", required = false) String point,
            @ApiParam(value = "A Radius defined in Metres (Default Value of 1km")
            @RequestParam(value = "radius", required = false, defaultValue = "1000.0") double radius) throws ParseException {

        AttributedZoneParameters azparams = new AttributedZoneParameters();
        Report resource = new Report();
        HttpStatus status = HttpStatus.OK;

        if (wkt != null) {
            azparams.BoundingBoxWkt = wkt;
        } else if (point != null) {
            String[] coords = point.split(",");
            if (coords.length >= 2) {
                azparams.point_x = Double.parseDouble(coords[0]);   
                azparams.point_x = Double.parseDouble(coords[1]);   
                azparams.radius = radius;
            } else {
                resource.addError("bad_point", "Point coordinate was ill formed, please supply in the form point=x,y");
                return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
            }
        }

        try {
            BooleanExpression predicates = AttributedZonePredicateBuilder.buildPredicates(azparams);
            Iterable<AttributedZone> zones = attributedZoneRepository.findAll(predicates);
            ReportAssembler assembler = new ReportAssembler(wkt, wkt);
            resource = assembler.toResource(zones);
        } catch (ParseException ex) {
            resource.addError("wkt_parser_err", ex.getLocalizedMessage());
            status = HttpStatus.BAD_REQUEST;
        }

        //resource.add(linkTo(methodOn(ReportController.class).search(wkt)).withSelfRel());
        return new ResponseEntity<>(resource, status);
    }
}
