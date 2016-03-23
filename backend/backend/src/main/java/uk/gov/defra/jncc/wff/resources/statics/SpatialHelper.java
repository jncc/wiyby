/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources.statics;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author felix
 */
public class SpatialHelper {

    public static final CoordinateReferenceSystem WSG84_SRS = DefaultGeographicCRS.WGS84;
   
    public static CoordinateReferenceSystem getOS_SRS() throws FactoryException
    {
        return CRS.decode("EPSG:27700");
    }
    
    
}
