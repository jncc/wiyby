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

/**
 *
 * @author Matt Debont
 */
@RestController
@RequestMapping(path = "/h", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class HelloRestController {
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getAll() {
        List<String> out = new ArrayList<>();
        out.add("HELLO");
        return out;
    }
}
