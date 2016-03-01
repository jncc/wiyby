/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import uk.gov.defra.jncc.wff.controllers.rest.NvzRestController;
import uk.gov.defra.jncc.wff.crud.entity.spatial.NitrateVulnerableZone;
import uk.gov.defra.jncc.wff.resources.NvzResource;

/**
 *
 * @author felix
 */
@Component
public class NvzResourceAssembler extends ResourceAssemblerSupport<NitrateVulnerableZone, NvzResource> {

    public NvzResourceAssembler() {
        super(NvzRestController.class, NvzResource.class);
    }
    
    @Override
    public NvzResource toResource(NitrateVulnerableZone z) {
        NvzResource resource = new NvzResource();
        resource.date = z.getDate();
        resource.date0 = z.getDate0();
        resource.fid = z.getFid();
        resource.geometryJson = z.getGeometryJson();
        resource.id = z.getId();
        resource.nvz2012 = z.getNvz2012();
        resource.type = z.getType();
        resource.uniqueId = z.getUniqueId();

        return resource;
    }

    
}