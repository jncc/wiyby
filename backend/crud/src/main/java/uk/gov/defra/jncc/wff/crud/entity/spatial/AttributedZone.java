package uk.gov.defra.jncc.wff.crud.entity.spatial;

import com.vividsolutions.jts.geom.Geometry;
import javax.persistence.Column;
import org.hibernate.annotations.Type;

/**
 *
 * @author Matt Debont
 */
public class AttributedZone {
    private String id;
    private String name;
    private String type;
    private String url;
    private String attributes;
   
    @Column(name="wkb_geometry", columnDefinition = "geometry(MultiPolygon,27700)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry geom;
    
    @Column(name="wkb_geometry_wgs84", columnDefinition = "geometry(MultiPolygon,4326)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry geom_wgs84;

    public AttributedZone() {
    }
    
    public AttributedZone(String id, String name, String type, String url, String attributes, Geometry geom, Geometry geom_wgs84) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.attributes = attributes;
        this.geom = geom;
        this.geom_wgs84 = geom_wgs84;
    }    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public Geometry getGeom_wgs84() {
        return geom_wgs84;
    }

    public void setGeom_wgs84(Geometry geom_wgs84) {
        this.geom_wgs84 = geom_wgs84;
    }
}
