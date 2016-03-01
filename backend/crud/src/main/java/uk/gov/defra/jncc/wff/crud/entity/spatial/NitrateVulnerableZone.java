package uk.gov.defra.jncc.wff.crud.entity.spatial;

import com.vividsolutions.jts.geom.Geometry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 *
 * @author felix
 */
@Entity
@Table(name = "nitrate_vulnerable_zones_view")
public class NitrateVulnerableZone {
    @Id
    @Column(name="ogc_fid")
    private int fid;
    
    @Column(name="nvz_type")
    private String type;
    
    @Column(name="nvz_id")
    private String id;
    
    @Column(name="nvz_2012")
    private String nvz2012;
    
    @Column(name="uniqueid")
    private String uniqueId;

    @Column(name="date")
    private String date;
    
    private String date0;
    
    @Column(name="wkb_geometry_json")
    private String geometryJson;
    
    @Column(name="wkb_geometry", columnDefinition = "geometry(MultiPolygon,27700)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry geom;

    /**
     * @return the fid
     */
    public int getFid() {
        return fid;
    }

    /**
     * @param fid the fid to set
     */
    public void setFid(int fid) {
        this.fid = fid;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nvz2012
     */
    public String getNvz2012() {
        return nvz2012;
    }

    /**
     * @param nvz2012 the nvz2012 to set
     */
    public void setNvz2012(String nvz2012) {
        this.nvz2012 = nvz2012;
    }

    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the date0
     */
    public String getDate0() {
        return date0;
    }

    /**
     * @param date0 the date0 to set
     */
    public void setDate0(String date0) {
        this.date0 = date0;
    }

    /**
     * @return the geometryJson
     */
    public String getGeometryJson() {
        return geometryJson;
    }

    /**
     * @param geometryJson the geometryJson to set
     */
    public void setGeometryJson(String geometryJson) {
        this.geometryJson = geometryJson;
    }

    /**
     * @return the geom
     */
    public Geometry getGeom() {
        return geom;
    }

    /**
     * @param geom the geom to set
     */
    public void setGeom(Geometry geom) {
        this.geom = geom;
    }
    
}
