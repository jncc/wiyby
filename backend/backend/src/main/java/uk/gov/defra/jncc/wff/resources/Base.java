package uk.gov.defra.jncc.wff.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.hateoas.ResourceSupport;

/**
 * Basic resource class with a simple message store for if the REST API needs
 * to return an error or generic message
 * 
 * @author Matt Debont
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Base extends ResourceSupport {
    public static final String ERROR = "error";
    private final Map<String, Map<String, String>> base;

    public Base() {
        base = new ConcurrentHashMap<>();
    }

    public Map<String, Map<String, String>> getBase() {
        return base;
    }
    
    /**
     * Add a Generic message of a give type, message is put into a Map with the 
     * code supplied and then put into a map under the key given by the supplied
     * type parameter
     * 
     * @param type The type of message i.e. INFO, WARN, ERROR, etc...
     * @param code A Code supplied for this message
     * @param msg The raw message to be returned
     */
    public void addMessage (String type, String code, String msg) {
        if (base.containsKey(type)) {
            base.get(type).put(code, msg);
        } else {
            Map<String, String> container = new ConcurrentHashMap<>();
            container.put(code, msg);
            base.put(type, container);
        }
    }
    
    /**
     * Add an error message to the base container to be returned, helper method
     * uses addMessage with predefined type
     * 
     * @param code The code for this error
     * @param msg The raw error message
     */
    public void addError(String code, String msg) {
        addMessage(ERROR, code, msg);
    }
}
