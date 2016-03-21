/*
 */
package uk.gov.defra.jncc.wff.resources;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.hateoas.ResourceSupport;

/**
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
    
    public void addMessage (String type, String code, String msg) {
        if (base.containsKey(type)) {
            base.get(type).put(code, msg);
        } else {
            Map<String, String> container = new ConcurrentHashMap<>();
            container.put(code, msg);
            base.put(type, container);
        }
    }
    
    public void addError(String code, String msg) {
        addMessage(ERROR, code, msg);
    }
}
