package uk.gov.defra.jncc.wff.resources;

import java.util.Map;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Matt Debont
 */
public class Report extends ResourceSupport {
    public String wkt;
    public Map<String, Map<String, String>> data;
}
