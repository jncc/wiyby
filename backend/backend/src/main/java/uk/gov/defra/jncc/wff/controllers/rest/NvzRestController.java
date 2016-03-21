/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.jncc.wff.crud.entity.spatial.NitrateVulnerableZone;
import uk.gov.defra.jncc.wff.resources.NvzResource;
import uk.gov.defra.jncc.wff.resources.assemblers.NvzResourceAssembler;
import uk.gov.defra.jncc.wff.crud.repository.NitrateVulnerableZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.defra.jncc.wff.crud.predicate.builders.NitrateVulnerableZonePredicateBuilder;
import uk.gov.defra.jncc.wff.crud.predicate.parameters.NitrateVulnerableZoneParameters;
import com.mysema.query.types.expr.BooleanExpression;
import com.vividsolutions.jts.io.ParseException;

/**
 *
 * @author felix
 */
@RestController
@RequestMapping(path = "/rest/nvz", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/rest/nvz", description = "Test out nvz spatial queries")
public class NvzRestController {
    @Autowired NitrateVulnerableZoneRepository nvzRepository;
    @Autowired NvzResourceAssembler nvzResourceAssembler;
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all Nitrate Vulnerable Zones", 
        response = NvzResource.class, 
        responseContainer = "Page")
    public HttpEntity<PagedResources<NvzResource>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<NitrateVulnerableZone> nvzs = nvzRepository.findAll(pageable);
        return new ResponseEntity<PagedResources<NvzResource>>(assembler.toResource(nvzs, nvzResourceAssembler), HttpStatus.OK);       
    }
    
    @ResponseBody
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all Nitrate Vulnerable Zones", 
        response = NvzResource.class, 
        responseContainer = "Page")
    public HttpEntity<PagedResources<NvzResource>> search(Pageable pageable, 
            PagedResourcesAssembler assembler,
            @ApiParam(value = "A WKT bounding box defined in OSGB (EPSG:4326)")
            @RequestParam(name = "wkt", required = false) String wkt) throws ParseException {
        
        NitrateVulnerableZoneParameters nvzparams = new NitrateVulnerableZoneParameters();
        nvzparams.BoundingBoxWkt = wkt;
        
        BooleanExpression predicates = NitrateVulnerableZonePredicateBuilder.buildPredicates(nvzparams);
        
        Page<NitrateVulnerableZone> nvzs = nvzRepository.findAll(predicates, pageable);
        return new ResponseEntity<PagedResources<NvzResource>>(assembler.toResource(nvzs, nvzResourceAssembler), HttpStatus.OK);       
    }
}

