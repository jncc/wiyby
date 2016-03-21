package uk.gov.defra.jncc.wff.resources;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matt Debont
 */
@ApiModel("Report Object")
public class Report extends Base {
    @ApiModelProperty(value = "A WKT representatation of the area covered by this report", required = false)
    public String wkt;
    @ApiModelProperty(value = "A human readable approximate location for this report", required = false)
    public String locality;
    @ApiModelProperty(value = "A List of mappings of matched rules in the form;"
            + "\"Rule\": \"Rule Matched\",\n" 
            + "\"Type\": \"Rule Type [statutory | recommended]\",\n"
            + "\"Heading\", \"Heading text for rule\",\n" 
            + "\"Text\", \"Text for matched rule\"", required = true)
    public List<Map<String, String>> data;
    @ApiModelProperty(value = "A map of rules types matched in this report", required = true)
    private Map<String, Boolean> ruleTypesMatched;

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

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }    

    public Map<String, Boolean> getRuleTypesMatched() {
        return ruleTypesMatched;
    }

    public void setRuleTypesMatched(Map<String, Boolean> ruleTypesMatched) {
        this.ruleTypesMatched = ruleTypesMatched;
    }
}
