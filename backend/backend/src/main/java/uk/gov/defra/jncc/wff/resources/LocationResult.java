/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources;

import java.util.List;

/**
 *
 * @author felix
 */
public class LocationResult {
    public String query;
    public List<Location> locations;

    public LocationResult(String query, List<Location> locations) {
        this.query = query;
        this.locations = locations;
    }
}
