package uk.gov.defra.jncc.wff.controllers.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.defra.jncc.wff.controllers.rest.LocationSearch;
import uk.gov.defra.jncc.wff.controllers.rest.ReportController;
import uk.gov.defra.jncc.wff.controllers.rest.WFSController;
import uk.gov.defra.jncc.wff.crud.repository.AttributedZoneRepository;
import uk.gov.defra.jncc.wff.resources.Base;
import uk.gov.defra.jncc.wff.resources.Location;
import uk.gov.defra.jncc.wff.resources.Report;

/**
 *
 * @author Matt Debont
 */
@Controller
public class ReportWebController {

    @Autowired AttributedZoneRepository attributedZoneRepository;
    @Autowired WFSController wfsController;
    @Autowired ReportController reportController;
    

    /**
     * Returns a report page for a given area selected by WKT
     * 
     * @param wkt The WKT for the area of this report in WGS84
     * @param model Auto-Injected model parameter
     * @return A report page or an error page if an error occurred 
     */
    @RequestMapping("/report")
    public String generateReport(
            @RequestParam(value = "wkt", required = true) String wkt,
            Model model) {
        ResponseEntity<Report> httpReport = reportController.search(wkt);
        if (httpReport.getStatusCode() == HttpStatus.OK) {
            Report resource = httpReport.getBody();

            resource.setWkt(attributedZoneRepository.getGeoJSON(wkt));
            

            
            model.addAttribute("resource", resource);
            model.addAttribute("wfd_catchment", wfsController.getLocations(wkt).getBody());

            return "report";
        } else {
            model.addAttribute("error", httpReport.getBody().getBase().get(Base.ERROR));
            return "error";
        }
    }
}
