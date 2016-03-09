package uk.gov.defra.jncc.wff.controllers.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.jncc.wff.resources.Report;
import uk.gov.defra.jncc.wff.resources.assemblers.ReportAssembler;
import uk.gov.defra.jncc.wff.resources.statics.FakeData;

/**
 *
 * @author Matt Debont
 */
@RestController
@RequestMapping(path = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/report", description = "Generates report objects for a given area for use by a frontend application")
public class ReportController {
    
    @ResponseBody
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all Nitrate Vulnerable Zones", 
        response = Report.class)
    public HttpEntity<Report> search(
            @ApiParam(value = "A WKT bounding box defined in OSGB (EPSG:4326)")
            @RequestParam(name = "wkt", required = false) String wkt) throws ParseException {
        
        ReportAssembler assembler = new ReportAssembler();
        //Report resource = assembler.toResource(FakeData.case1());
        //resource = assembler.toResource(FakeData.case2());
        Report resource = assembler.toResource(FakeData.case3());
        //resource = assembler.toResource(FakeData.case4());
        
        resource.add(linkTo(methodOn(ReportController.class).search(wkt)).withSelfRel());
        
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
