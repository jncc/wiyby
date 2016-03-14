package uk.gov.defra.jncc.wff.resources.assemblers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import uk.gov.defra.jncc.wff.controllers.rest.ReportController;
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;
import uk.gov.defra.jncc.wff.resources.Report;
import uk.gov.defra.jncc.wff.resources.statics.CodeMappings;

/**
 *
 * @author Matt Debont
 */
public class ReportAssembler extends ResourceAssemblerSupport<Iterable<AttributedZone>, Report> {

    private static final Map<String, String> MAPPINGS = CodeMappings.mappings();
    private String wkt;
    private String locality;

    public ReportAssembler() {
        super(ReportController.class, Report.class);
    }

    public ReportAssembler(String wkt, String locality) {
        super(ReportController.class, Report.class);
        this.wkt = wkt;
        this.locality = locality;
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

    @Override
    public Report toResource(Iterable<AttributedZone> zones) {
        // Turn returned Iterable into a stream
        Stream<AttributedZone> zoneStream = StreamSupport.stream(zones.spliterator(), false);

        List<String> types = zoneStream
                .filter(distinctByKey((zone) -> zone.getType()))
                .map((zone) -> zone.getType())
                .collect(Collectors.toList());

        Map<String, List<AttributedZone>> zoneOutputList = new ConcurrentHashMap<>();
        Map<String, List<String>> codeOutputList = new ConcurrentHashMap<>();

        types.stream().forEach((String type) -> {
            List<String> codes = new ArrayList<>();
            // Create a new stream from the Iterable for use in processing
            Stream<AttributedZone> zonesProc = StreamSupport.stream(zones.spliterator(), false);
            List<AttributedZone> typeZones = zonesProc
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
            //codeOutputList.putIfAbsent(type, codes.stream().distinct().map((code) -> MAPPINGS.get(code)).collect(Collectors.toList()));
            codeOutputList.putIfAbsent(type, codes.stream().distinct().collect(Collectors.toList()));
        });

        Report output = new Report();
        output.setData(applyRules(zoneOutputList, codeOutputList));

        if (wkt != null && !wkt.isEmpty() && locality != null && !locality.isEmpty()) {
            output.setWkt(wkt);
            output.setLocality(locality);
        }

        return output;
    }

    private List<Map<String, String>> applyRules(
            Map<String, List<AttributedZone>> zoneOutputs,
            Map<String, List<String>> codeOutputs) {
        List<Map<String, String>> finalList = new ArrayList<>();

        if (codeOutputs.containsKey("NVZ")) {
            finalList.add(Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Rule", "NVZ"),
                    new SimpleEntry<>("Type", "Statutory"),
                    new SimpleEntry<>("Heading", "This farm has to comply with the Action Programme Measures for Nitrates"),
                    new SimpleEntry<>("Text", "This means you must read and understand what is required of you including the <a href=\"#\">preparation of a Risk Map</a><br/>Download: <a href=\"#\">latest Nitrate Vulnerable Zone guidance</a>")
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        if (codeOutputs.containsKey("SGZ_SW")) {
            if (codeOutputs.get("SGZ_SW").isEmpty()) {
                finalList.add(Collections.unmodifiableMap(Stream.of(
                        new SimpleEntry<>("Rule", "SGZ_SW_NO_RISK"),
                        new SimpleEntry<>("Type", "Recommended"),
                        new SimpleEntry<>("Heading", "This land is in a drinking water catchment which is currently not at risk from any pollutants."),
                        new SimpleEntry<>("Text", String.format("Water from this land provides drinking water to people living in and around %s. It is not currrently at risk, but to keep your drinking water safe please continue to follow best practice<br/>Vist the <a href=\"#\">Best Practice site</a>", this.locality))
                ).collect(
                        Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
                )));
            } else {
                String codeText = mapCodesToText(codeOutputs.get("SGZ_SW"));
                String urls = zoneOutputs.get("SGZ_SW").stream().map((e) -> String.format("Visit the <a href=\"%s\">Safeguard Zone Action Plan for %s</a>", e.getUrl(), e.getName())).collect(Collectors.joining("<br />"));
                finalList.add(Collections.unmodifiableMap(Stream.of(
                        new SimpleEntry<>("Rule", "SGZ_SW"),
                        new SimpleEntry<>("Type", "Recommended"),
                        new SimpleEntry<>("Heading", String.format("This land is within a surface water safeguard zone which protects drinking water from the following %s.", codeText)),
                        new SimpleEntry<>("Text", String.format("Water from this land provides drinking water to people living in and around %s. It is an area where %s must be used with care. Here’s what you can do to minimise the risk by changing the way you use pesticides and nutrients.<br/>%s", locality, codeText, urls))
                ).collect(
                        Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
                )));
            }
        }

        if (codeOutputs.containsKey("SGZ_GW")) {
            String codeText = mapCodesToText(codeOutputs.get("SGZ_GW"));
            String urls = zoneOutputs.get("SGZ_GW").stream().map((e) -> String.format("Visit the <a href=\"%s\">Safeguard Zone Action Plan for %s</a>", e.getUrl(), e.getName())).collect(Collectors.joining("<br/>"));
            finalList.add(Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Rule", "SGZ_GW"),
                    new SimpleEntry<>("Type", "Recommended"),
                    new SimpleEntry<>("Heading", String.format("This land is within a groundwater safeguard zone which protects drinking water from the following %s.", codeText)),
                    new SimpleEntry<>("Text", String.format("Water from this land provides drinking water to people living in and around %s. It is an area where %s must be used with care. Here’s what you can do to minimise the risk by changing the way you use pesticides and nutrients.<br/>%s", locality, codeText, urls))
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        if (!codeOutputs.containsKey("NVZ") && !codeOutputs.containsKey("SGZ_GW") && !codeOutputs.containsKey("SGZ_SW")) {
            finalList.add(Collections.unmodifiableMap(Stream.of(
                    new SimpleEntry<>("Rule", "NONE"),
                    new SimpleEntry<>("Type", "Recommended"),
                    new SimpleEntry<>("Heading", String.format("Look after your environment")),
                    new SimpleEntry<>("Text", String.format("See the links below for ways to get the best out of your environment<br/>Vist the <a href=\"\">Generic Advice Site</a>"))
            ).collect(
                    Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())
            )));
        }

        return finalList;
    }

    private String mapCodesToText(List<String> codes) {
        return codes.stream().collect(Collectors.joining(", "));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
