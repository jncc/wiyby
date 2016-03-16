/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

/**
 *
 * @author felix
 */
@Service
public class ReprojectionService {

    public String ReprojectBbox(String bbox, int srs) throws FactoryException, MismatchedDimensionException, TransformException {
        String[] elements = bbox.split(",");
        
        if (elements.length != 4) throw new RuntimeException("invalid bounding box"); 
        
        double[] xsrc = new double[2];
        
        xsrc[0] = Double.parseDouble(elements[0]);
        xsrc[1] = Double.parseDouble(elements[1]);
        
        double[] x = ReprojectCoordinate(xsrc, srs);
        
        double[] ysrc = new double[2];
        
        ysrc[0] = Double.parseDouble(elements[2]);
        ysrc[1] = Double.parseDouble(elements[3]);
        
        double[] y = ReprojectCoordinate(ysrc, srs);
        
        return x[0] + "," + x[1] + "," + y[0] + "," + y[1];
    }

      

    private double[] ReprojectCoordinate(double[] coordinate, int srs) throws FactoryException, MismatchedDimensionException, TransformException {
        DirectPosition input = new GeneralDirectPosition(coordinate[0], coordinate[1]);
        
        String sourceSrs = String.valueOf(srs);
        
        CRSAuthorityFactory crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",null);
        CoordinateReferenceSystem targetCrs = crsFac.createCoordinateReferenceSystem("27700");
        CoordinateReferenceSystem sourceCrs = crsFac.createCoordinateReferenceSystem(sourceSrs);
        
        MathTransform transformer = new DefaultCoordinateOperationFactory().createOperation(sourceCrs, targetCrs).getMathTransform();
        
        return transformer.transform(input, input).getCoordinates();
        
    }
    
    
}
