/*
 */
package uk.gov.defra.jncc.wff.resources.statics;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.gov.defra.jncc.wff.crud.entity.spatial.AttributedZone;

/**
 *
 * @author Matt Debont
 */
public class FakeData {

    public static AttributedZone SGZ_GW_1 = new AttributedZone("SGZ_GW_1", "SGZ_GW_1", "SGZ_GW", "http://ea/sgz_gw_1", "CODE_ABCD, NITR_ABCD, PEST_ABCD", null, null);
    public static AttributedZone SGZ_GW_2 = new AttributedZone("SGZ_GW_2", "SGZ_GW_2", "SGZ_GW", "http://ea/sgz_gw_2", "NITR_ABCD", null, null);

    public static AttributedZone SGZ_SW_1 = new AttributedZone("SGZ_SW_1", "SGZ_SW_1", "SGZ_SW", "http://ea/sgz_sw_1", "CODE_ABCD, NITR_ABCD, PEST_ABCD", null, null);
    public static AttributedZone SGZ_SW_2 = new AttributedZone("SGZ_SW_1", "SGZ_SW_1", "SGZ_SW", "http://ea/sgz_sw_2", "PEST_ABCD", null, null);
    public static AttributedZone SGZ_SW_3 = new AttributedZone("SGZ_SW_1", "SGZ_SW_1", "SGZ_SW", "http://ea/sgz_sw_3", null, null, null);

    public static AttributedZone NVZ = new AttributedZone("NVZ_1", "NVZ_1", "NVZ", "http://ea/nvz_1", null, null, null);

    public static List<AttributedZone> case1() {
        return Collections.unmodifiableList(Stream.of(
                NVZ
        ).collect(Collectors.toList()));
    }

    public static List<AttributedZone> case2() {
        return Collections.unmodifiableList(Stream.of(
                NVZ,
                SGZ_GW_1,
                SGZ_SW_1
        ).collect(Collectors.toList()));
    }

    public static List<AttributedZone> case3() {
        return Collections.unmodifiableList(Stream.of(
                SGZ_SW_1,
                SGZ_SW_3
        ).collect(Collectors.toList()));
    }
    
    public static List<AttributedZone> case4() {
        return Collections.unmodifiableList(Stream.of(
                SGZ_GW_2,
                SGZ_SW_3
        ).collect(Collectors.toList()));
    }    
}
