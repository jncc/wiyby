/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.web;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.defra.jncc.wff.controllers.rest.LocationSearch;
import uk.gov.defra.jncc.wff.resources.Base;
import uk.gov.defra.jncc.wff.resources.Location;
import uk.gov.defra.jncc.wff.resources.LocationResult;

/**
 *
 * @author felix
 */
@Controller
public class LocationController {

    @Autowired
    LocationSearch locationSearch;

    @RequestMapping(value = "/web/location", method = RequestMethod.GET)
    public String GetLocationsForm(Model model) {
        model.addAttribute("LocationResult", new LocationResult());

        return "find";
    }

    @RequestMapping(value = "/web/location", method = RequestMethod.POST)
    public String GetLocationsSubmit(
            @ModelAttribute LocationResult search,
            Model model) throws Exception {
        
        //humm try catch but don't want to reveal error to end user?
        ResponseEntity<LocationResult> httpSearchResult = locationSearch.getLocationByName(search.getQuery());

        if (httpSearchResult.getStatusCode() == HttpStatus.OK) {
            LocationResult searchResult = httpSearchResult.getBody();

            if (searchResult.getLocations().size() == 1) {
                //redirct to map page with sigle result
            }

            List<Location> topLocations = searchResult.getLocations().stream()
                    .limit(5)
                    .collect(Collectors.toList());

            LocationResult result = new LocationResult();
            
            result.setQuery(searchResult.getQuery());
            result.setLocations(topLocations);
            
            model.addAttribute("LocationResult", result);

            return "locations";

        } else {
            model.addAttribute("error", httpSearchResult.getBody().getBase().get(Base.ERROR));
            return "error";
        }
    }
}
