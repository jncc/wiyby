/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.services;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;
import uk.gov.defra.jncc.wff.resources.statics.SpatialHelper;

/**
 *
 * @author felix
 */
@Service
public class EnvelopeGeneratorService {
    
    public Point GetOSCentroidFromPolygon (String wkt) throws ParseException, FactoryException, MismatchedDimensionException, TransformException 
    {
        Geometry polygon = GetGeometryFromWKT(wkt);
        
        Geometry sourceGeom = polygon.getCentroid();
        
        Geometry targetGeometry = TransformGeometry(sourceGeom);
        
        return (Point)targetGeometry;
    }

    public Envelope GetOSEvelopeFromPolygon(String wkt) throws ParseException, FactoryException, MismatchedDimensionException, TransformException 
    {
        Geometry polygon = GetGeometryFromWKT(wkt);
        
        Geometry sourceGeom = polygon.getEnvelope();
        
        Geometry targetGeometry = TransformGeometry(sourceGeom);
        
        return targetGeometry.getEnvelopeInternal();
    }
    
    private Geometry TransformGeometry(Geometry sourceGeom) throws FactoryException, MismatchedDimensionException, TransformException
    {
        MathTransform transform = CRS.findMathTransform(SpatialHelper.WSG84_SRS, SpatialHelper.getOS_SRS());
        return JTS.transform(sourceGeom, transform);
    }
    
    private Geometry GetGeometryFromWKT(String wkt) throws ParseException
    {
        WKTReader fromText = new WKTReader();
        return fromText.read(wkt);
    }

      

    
}
