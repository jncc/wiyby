package uk.gov.defra.jncc.wff.resources.assemblers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import uk.gov.defra.jncc.wff.controllers.rest.ReportController;
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;
import uk.gov.defra.jncc.wff.resources.Report;
import uk.gov.defra.jncc.wff.resources.statics.CodeMappings;

/**
 *
 * @author Matt Debont
 */
public class ReportAssembler extends ResourceAssemblerSupport<List<AttributedZone>, Report> {

    private static final Map<String, String> mappings = CodeMappings.mappings();

    public ReportAssembler() {
        super(ReportController.class, Report.class);
    }

    @Override
    public Report toResource(List<AttributedZone> zones) {

        List<String> types = zones.stream()
                .filter(distinctByKey((zone) -> zone.getType()))
                .map((zone) -> zone.getType())
                .collect(Collectors.toList());

        Map<String, List<AttributedZone>> zoneOutputList = new ConcurrentHashMap<>();
        Map<String, List<String>> codeOutputList = new ConcurrentHashMap<>();

        types.stream().forEach((String type) -> {
            List<String> codes = new ArrayList<>();
            List<AttributedZone> typeZones = zones.stream()
                    .filter((zone) -> zone.getType().equals(type))
                    .collect(Collectors.toList());

            typeZones.stream()
                    .map((zone) -> zone.getAttributes())
                    .collect(Collectors.toList())
                    .forEach((String attribs) -> {
                        if (attribs != null) {
                            for (String attrib : attribs.split(",")) {
                                codes.add(attrib.trim());
                            }
                        }
                    });

            zoneOutputList.putIfAbsent(type, typeZones);
            codeOutputList.putIfAbsent(type, codes.stream().distinct().map((code) -> mappings.get(code)).collect(Collectors.toList()));
        });

        Report output = new Report();
        output.data = applyRules(zoneOutputList, codeOutputList);

        return output;
    }

    private Map<String, Map<String, String>> applyRules(
            Map<String, List<AttributedZone>> zoneOutputs, 
            Map<String, List<String>> codeOutputs) {
        Map<String, Map<String, String>> finalMap = new ConcurrentHashMap<>();

        if (codeOutputs.containsKey("NVZ")) {
            finalMap.put("NVZ", Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Heading", "This farm has to comply with the Action Programme Measures for Nitrates"),
                    new SimpleEntry<>("Text", "This means you must read and understand what is required of you including the <a href=\"#\">preparation of a Risk Map</a><br/>Download: <a href=\"#\">latest Nitrate Vulnerable Zone guidance</a>")
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        if (codeOutputs.containsKey("SGZ_SW")) {
            if (codeOutputs.get("SGZ_SW").isEmpty()) {
                finalMap.put("SGZ_SW", Collections.unmodifiableMap(Stream.of(
                        new SimpleEntry<>("Heading", "This land is in a drinking water catchment which is currently not at risk from any pollutants."),
                        new SimpleEntry<>("Text", "Water from this land provides drinking water to people living in and around <<Locality>>. It is not currrently at risk, but to keep your drinking water safe please continue to follow best practice<br/>Vist the <a href=\"#\">Best Practice site</a>")
                ).collect(
                        Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
                )));
            } else {
                String codeText;
                codeText = mapCodesToText(codeOutputs.get("SGZ_SW"));
                finalMap.put("SGZ_SW", Collections.unmodifiableMap(Stream.of(
                        new SimpleEntry<>("Heading", String.format("This land is within a surface water safeguard zone which protects drinking water from the following %s.", codeText)),
                        new SimpleEntry<>("Text", String.format("Water from this land provides drinking water to people living in and around <<Locality>>. It is an area where %s must be used with care. Here’s what you can do to minimise the risk by changing the way you use pesticides and nutrients.<br/>Visit the <a href=\"\">Safeguard Zone Action Plan for <<SGZ_Name>></a>", codeText))
                ).collect(
                        Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
                )));
            }
        }

        if (codeOutputs.containsKey("SGZ_GW")) {
            String codeText = mapCodesToText(codeOutputs.get("SGZ_GW"));
            finalMap.put("SGZ_GW", Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Heading", String.format("This land is within a groundwater safeguard zone which protects drinking water from the following %s.", codeText)),
                    new SimpleEntry<>("Text", String.format("Water from this land provides drinking water to people living in and around <<Locality>>. It is an area where %s must be used with care. Here’s what you can do to minimise the risk by changing the way you use pesticides and nutrients.<br/>Visit the <a href=\"\">Safeguard Zone Action Plan for <<SGZ_Name>></a>", codeText))
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        if (!codeOutputs.containsKey("NVZ") && !codeOutputs.containsKey("SGZ_GW") && !codeOutputs.containsKey("SGZ_SW")) {
            finalMap.put("NONE", Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Heading", String.format("Look after your environment")),
                    new SimpleEntry<>("Text", String.format("See the links below for ways to get the best out of your environment<br/>Vist the <a href=\"\">Generic Advice Site</a>"))
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        return finalMap;
    }

    private String mapCodesToText(List<String> codes) {
        StringJoiner sj = new StringJoiner(", ");
        codes.stream().forEach((code) -> {
            sj.add(code);
        });
        return sj.toString();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
