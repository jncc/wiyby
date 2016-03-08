package uk.gov.defra.jncc.wff.resources.statics;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Matt Debont
 */
public class CodeMappings {
    public static Map<String, String> mappings() {
        return Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>("CODE_ABCD", "CODE_ABCD Actual"),
                new SimpleEntry<>("NITR_ABCD", "NITR_ABCD Actual"),
                new SimpleEntry<>("PEST_ABCD", "PEST_ABCD Actual")
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            ));
    }
    
    public static List<String> layerTypes() {
        return Collections.unmodifiableList(Stream.of(
                "NVZ",
                "SGZ_GW",
                "SGZ_SW"
            ).collect(Collectors.toList()));        
    }
}