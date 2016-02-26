/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
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

/**
 *
 * @author felix
 */
@RestController
@RequestMapping(path = "/nvz", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Api(value = "/nvz", description = "Test out nvz spatial queries")
public class NvzRestController {
    @Autowired NitrateVulnerableZoneRepository nvzRepository;
    @Autowired NvzResourceAssembler nvzResourceAssembler;
//    @Autowired ApiConfiguration configuration;
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all Nitrate Vulnerable Zones", 
        response = NvzResource.class, 
        responseContainer = "Page")
    public HttpEntity<List<NvzResource>> getAll() {
        Iterable<NitrateVulnerableZone> nvzones = nvzRepository.findAll();

        return new ResponseEntity<List<NvzResource>>(nvzResourceAssembler.toResources(nvzones), HttpStatus.OK); 
        
        
    }
}
