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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import com.springframework.samples.petclinic.models.*;

@Controller
class VisitController {

	@Autowired
	private RestTemplate restTemplate;
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Pet object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param petId
     * @return Pet
     */
    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
    	System.out.println("MAIN APP - VISIT CONTROLLER, ModelAttribute(visit), START....");   
    	String url = URIConstants.VISIT_URL + "/owners/*/pets/{petId}/visits/visit"; 
    	PetVisitRequestBody response = restTemplate.getForObject(url, PetVisitRequestBody.class, petId);
        model.put("pet", response.getPet());
        return response.getVisit();
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
    @RequestMapping(value = "/owners/*/pets/{petId}/visits/new", method = RequestMethod.GET)
    public String initNewVisitForm(@PathVariable("petId") int petId) {
    	System.out.println("MAIN APP - VISIT CONTROLLER, GET VISIT FORM, START....");   
    	return URIConstants.CREATE_VISIT_HTML;
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @RequestMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new", method = RequestMethod.POST)
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
    	System.out.println("MAIN APP - VISIT CONTROLLER, POST VISIT, START....");
    	if (result.hasErrors()) {
            return URIConstants.CREATE_VISIT_HTML;
        } else {
        	String url = URIConstants.VISIT_URL + "/owners/*/pets/*/visits/new";
        	System.out.println("MAIN APP - VISIT CONTROLLER, VISIT ToString()....");
        	System.out.println(visit.getDescription());
        	restTemplate.postForEntity(url, visit, Visit.class);            
            return "redirect:/owners/{ownerId}";
        }
    }
}
