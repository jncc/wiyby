package uk.gov.defra.jncc.wff.controllers.rest;

import com.mysema.query.types.expr.BooleanExpression;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import uk.gov.defra.jncc.wff.resources.Base;
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

    @Autowired AttributedZoneRepository attributedZoneRepository;

    /**
     * Generates an environmental report for a given area defined in WKT (WGS84)
     *
     * @param wkt WKT in WGS84 for a given query area
     * @return A Report object (if no error occurs) or a Base object which 
     * contains messages
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Generates a report object for a given area, either defined as a WKT polygon in WGS84 (EPSG:4326)",
            response = Report.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created a report for the give input parameters", response = Report.class),
        @ApiResponse(code = 400, message = "API was given poorly formated inputs, messages object should contain an error entry with the full error message", response = Base.class)
    })
    public ResponseEntity<Report> search(
            @ApiParam(value = "A WKT bounding box defined in EPSG:4326")
            @RequestParam(name = "wkt", required = true) String wkt) {

        AttributedZoneParameters azparams = new AttributedZoneParameters();
        Report resource = new Report();
        HttpStatus status = HttpStatus.OK;

        azparams.BoundingBoxWkt = wkt;

        try {
            BooleanExpression predicates = AttributedZonePredicateBuilder.buildPredicates(azparams);
            Iterable<AttributedZone> zones = attributedZoneRepository.findAll(predicates);
            ReportAssembler assembler = new ReportAssembler(wkt, wkt);
            resource = assembler.toResource(zones);
        } catch (ParseException ex) {
            resource.addError("wkt_parser_err", ex.getLocalizedMessage());
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(resource, status);
    }
}
