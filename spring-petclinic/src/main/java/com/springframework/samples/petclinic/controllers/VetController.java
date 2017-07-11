/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springframework.samples.petclinic.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.springframework.samples.petclinic.models.Owner;
import com.springframework.samples.petclinic.models.Vets;
import com.springframework.samples.petclinic.models.Vet;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {
	@Autowired
	private RestTemplate restTemplate;	
     
    @RequestMapping(value = {URIConstants.VET_HTML})
    public String showVetList(Map<String, Object> model) {
    	System.out.println("MAIN APP - PET CONTROLLER, GET VETS, START....");   
    	String url = URIConstants.VET_URL + URIConstants.GET_VETS;
    	ResponseEntity<Vets> responseEntity = restTemplate.getForEntity(url, Vets.class);
    	model.put("vets", responseEntity.getBody());
        return URIConstants.SHOW_VET_HTML;
    }

    @RequestMapping(value = { URIConstants.VET_JSON })
    public @ResponseBody Vets showResourcesVetList() {
    	System.out.println("MAIN APP - PET CONTROLLER, Show json/xml START....");
    	
    	// Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
    	String url = URIConstants.VET_URL + URIConstants.GET_VETS;    	
    	ResponseEntity<Vets> responseEntity = restTemplate.getForEntity(url, Vets.class);
    	return responseEntity.getBody();
    }
}
