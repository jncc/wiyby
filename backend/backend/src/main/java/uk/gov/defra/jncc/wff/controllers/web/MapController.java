package uk.gov.defra.jncc.wff.controllers.web;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    AttributedZoneRepository attributedZoneRepository;

    @RequestMapping("/map")
    public String generateReport(
            @RequestParam(value = "wkt", required = false) String wkt,
            @RequestParam(value = "point", required = false) String point,
            @RequestParam(value = "radius", required = false) String radius,
            Model model) throws Exception {

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
        } else if (point != null && radius != null && !point.isEmpty() && !radius.isEmpty()) {
            String[] coords = point.split(",");
            if (coords.length >= 2) {
                model.addAttribute("lat", Double.parseDouble(coords[1]));
                model.addAttribute("long", Double.parseDouble(coords[0]));
                model.addAttribute("radius", radius);
            } else {
                model.addAttribute("error", "Point coordinate was ill formed, please supply in the form point=x,y");
                return "error";
            }
        }

        return "map";
    }
}
