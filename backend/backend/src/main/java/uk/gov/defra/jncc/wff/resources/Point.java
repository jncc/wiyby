/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felix
 */
public class Point {
    public List<Double> coordinates;
    public String type = "point";
    
    public Point(double x, double y) {
        coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);
    }
}
