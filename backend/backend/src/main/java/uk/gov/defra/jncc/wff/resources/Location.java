/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources;

import uk.gov.defra.jncc.wff.resources.Centroid;

/**
 *
 * @author felix
 */
public class Location {
    public Location()
    {
        this.centroid = new Centroid();
    }
    
    public String osId;
    public String name;
    public Centroid centroid;
}
