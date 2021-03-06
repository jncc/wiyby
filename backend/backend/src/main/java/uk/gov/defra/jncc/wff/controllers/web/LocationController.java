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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String GetLocationsForm(Model model) {
        model.addAttribute("LocationResult", new LocationResult());

        return "find";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String GetLocationsSubmit(
            @ModelAttribute LocationResult search,
            Model model, RedirectAttributes redirectAttributes) throws Exception {
        
        //humm try catch but don't want to reveal error to end user?
        ResponseEntity<LocationResult> httpSearchResult = locationSearch.getLocation(search.getQuery(), null, null);

        if (httpSearchResult.getStatusCode() == HttpStatus.OK) {
            LocationResult searchResult = httpSearchResult.getBody();

            if (searchResult.getLocations().size() == 1) {
                redirectAttributes.addAttribute("wkt", searchResult.getLocations().get(0).wktBbox);
                return "redirect:/map";
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
