package uk.gov.defra.jncc.wff.controllers.web;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.defra.jncc.wff.crud.repository.AttributedZoneRepository;
import uk.gov.defra.jncc.wff.resources.LocationResult;
import uk.gov.defra.jncc.wff.resources.statics.SpatialHelper;

/**
 *
 * @author Matt Debont
 */
@Controller
public class MapController {

    @Autowired
    AttributedZoneRepository attributedZoneRepository;

    /**
     * Controller for the map page, takes in nothing (interactive map mode) or
     * takes in a WKT for a pre-selected area
     *
     * @param wkt A WKT string in WGS84
     * @param model Auto-Injected model parameter
     * @return The map page or an error page if some error occurs
     */
    @RequestMapping("/map")
    public String map(
            @RequestParam(value = "wkt", required = false) String wkt,
            Model model) {

        try {
            generateModel(wkt, model);
        } catch (ParseException | IOException ex) {
            model.addAttribute("error", ex.getLocalizedMessage());
            return "error";
        }

        return "map";
    }

    @RequestMapping("/imap")
    public String interactiveMap(
            @RequestParam(value = "wkt", required = false) String wkt,
            Model model) {
        try {
            generateModel(wkt, model);
        } catch (ParseException | IOException ex) {
            model.addAttribute("error", ex.getLocalizedMessage());
            return "error";
        }
        return "imap";
    }

    private void generateModel(String wkt, Model model) throws IOException, ParseException {
        if (wkt != null && !wkt.isEmpty()) {
            // WKT element provided try and use that
            model.addAttribute("geojson", SpatialHelper.getGeoJSON(SpatialHelper.getGeometryFromWKT(wkt, 4326)));
        }
    }
}
