package uk.gov.defra.jncc.wff.resources;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Matt Debont
 */
public class Report extends Base {
    public String wkt;
    public String locality;
    public List<Map<String, String>> data;

    public Report() {
    }
   
    public Report(String wkt, String locality, List<Map<String, String>> data) {
        this.wkt = wkt;
        this.locality = locality;
        this.data = data;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData( List<Map<String, String>> data) {
        this.data = data;
    }    
}
