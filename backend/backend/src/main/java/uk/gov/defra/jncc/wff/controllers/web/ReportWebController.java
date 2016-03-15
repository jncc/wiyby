/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.web;

import com.mysema.query.types.expr.BooleanExpression;
import com.vividsolutions.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@Controller
public class ReportWebController {
    @Autowired AttributedZoneRepository attributedZoneRepository;
    
    @RequestMapping("/web/report")
    public String generateReport(
            @RequestParam(value="wkt", required=true) String wkt,
            @RequestParam(value="locality", required=false) String locality, 
            Model model) {
        AttributedZoneParameters azparams = new AttributedZoneParameters();
        azparams.BoundingBoxWkt = wkt;

        Report resource;
        
        String geojson = attributedZoneRepository.getGeoJSON(wkt);
        if (locality == null || locality.isEmpty()) {
            locality = geojson;
        }

        try {
            BooleanExpression predicates = AttributedZonePredicateBuilder.buildPredicates(azparams);
            Iterable<AttributedZone> zones = attributedZoneRepository.findAll(predicates);
            ReportAssembler assembler = new ReportAssembler(geojson, locality);
            resource = assembler.toResource(zones);
            
        } catch (ParseException ex) {
            model.addAttribute("error", ex.getLocalizedMessage());
            return "error";
        }

        model.addAttribute("resource", resource);

        return "report";
    }
}
