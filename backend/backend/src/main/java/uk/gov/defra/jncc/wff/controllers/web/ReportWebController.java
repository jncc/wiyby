/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.defra.jncc.wff.controllers.rest.ReportController;
import uk.gov.defra.jncc.wff.controllers.rest.WFSController;
import uk.gov.defra.jncc.wff.crud.predicate.parameters.AttributedZoneParameters;
import uk.gov.defra.jncc.wff.crud.repository.AttributedZoneRepository;
import uk.gov.defra.jncc.wff.resources.Base;
import uk.gov.defra.jncc.wff.resources.Report;

/**
 *
 * @author Matt Debont
 */
@Controller
public class ReportWebController {

    @Autowired
    AttributedZoneRepository attributedZoneRepository;
    @Autowired
    WFSController wfsController;
    @Autowired
    ReportController reportController;

    @RequestMapping("/web/report")
    public String generateReport(
            @RequestParam(value = "wkt", required = true) String wkt,
            @RequestParam(value = "locality", required = false) String locality,
            Model model) throws Exception {
        AttributedZoneParameters azparams = new AttributedZoneParameters();
        azparams.BoundingBoxWkt = wkt;

        ResponseEntity<Report> httpReport = reportController.search(wkt);
        if (httpReport.getStatusCode() == HttpStatus.OK) {
            Report resource = httpReport.getBody();
            
            String geojson = attributedZoneRepository.getGeoJSON(wkt);
            if (locality == null || locality.isEmpty()) {
                locality = geojson;
                resource.setLocality(locality);
            } else {
                resource.setLocality(locality);
            }
            
            resource.setWkt(geojson);

            model.addAttribute("resource", resource);
            model.addAttribute("wfd_catchment", wfsController.getLocations(wkt).getBody());

            return "report";
        } else {
            model.addAttribute("error", httpReport.getBody().getBase().get(Base.ERROR));
            return "error";
        }
    }
}
