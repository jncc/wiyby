/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.defra.jncc.wff.crud.repository.AttributedZoneRepository;

/**
 *
 * @author Matt Debont
 */
@Controller
public class MapController {

    @Autowired AttributedZoneRepository attributedZoneRepository;
    
    @RequestMapping("/map")
    public String generateReport(
            @RequestParam(value = "wkt", required = false) String wkt,
            @RequestParam(value = "point", required = false) String point,
            @RequestParam(value = "radius", required = false) String radius,            
            Model model) throws Exception {
                
        if (wkt != null && !wkt.isEmpty()) {
            // WKT element provided try and use that
             model.addAttribute("geojson", 
                     attributedZoneRepository.getGeoJSON(
                             "POLYGON((0.594292 52.733381, 0.942764 52.733381, 0.942764 52.628062, 0.594292 52.628062, 0.594292 52.733381))"));
        } else if (point != null && radius != null && !point.isEmpty() && !radius.isEmpty()) {
            model.addAttribute("point", point);
            model.addAttribute("radius", radius);
        }

        return "map";
    }
}
