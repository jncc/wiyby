package uk.gov.defra.jncc.wff.controllers.web;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
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

    /**
     * Controller for the map page, takes in nothing (interactive map mode) or 
     * takes in a WKT for a pre-selected area
     * 
     * @param wkt A WKT string in WGS84
     * @param model Auto-Injected model parameter
     * @return The map page or an error page if some error occurs
     */
    @RequestMapping("/map")
    public String generateReport(
            @RequestParam(value = "wkt", required = false) String wkt, Model model) {

        if (wkt != null && !wkt.isEmpty()) {
            // WKT element provided try and use that
            try {
                WKTReader fromText = new WKTReader();
                Geometry geom = fromText.read(wkt);
                model.addAttribute("geojson", attributedZoneRepository.getGeoJSON(geom));
            } catch (ParseException ex) {
                model.addAttribute("error", ex.getLocalizedMessage());
                return "error";
            }
        }

        return "map";
    }
}
