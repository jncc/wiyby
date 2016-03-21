/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.controllers.rest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.defra.jncc.wff.resources.Hello;

/**
 *
 * @author Matt Debont
 */
@RestController
@RequestMapping(path = "/rest/hi", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class HelloRestController {
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Hello> getAll() {
        List<Hello> out = new ArrayList<>();
        out.add(new Hello("world"));
        out.add(new Hello("EA"));
        out.add(new Hello("Farmers"));
        return out;
    }
}
