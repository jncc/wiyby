package uk.gov.defra.jncc.wff.resources;

import java.util.Map;

/**
 *
 * @author Matt Debont
 */
public class Report extends Base {
    private String wkt;
    private String locality;
    private Map<String, Map<String, String>> data;

    public Report() {
    }
   
    public Report(String wkt, String locality, Map<String, Map<String, String>> data) {
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

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }    
}
