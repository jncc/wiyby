/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author felix
 */
public class NvzResource extends ResourceSupport {
    public int fid;
    public String type;
    public String id;
    public String nvz2012;
    public String uniqueId;
    public String date;
    public String date0;
    public String geometryJson;
}
