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
package com.springframework.samples.petclinic.visit;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springframework.samples.petclinic.visit.URIConstants;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@RestController
@Transactional
class VisitController {

	private RestTemplate restTemplate;
    private final VisitRepository visits;

    @Autowired
    public VisitController(VisitRepository visits) {
        this.visits = visits;
        restTemplate = new RestTemplate();        
    }

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
    @RequestMapping(value = "/owners/*/pets/{petId}/visits/visit", method = RequestMethod.GET)
    public PetVisitRequestBody loadPetWithVisit(@PathVariable("petId") int petId) {
    	System.out.println("SERVICE visit-controller.... METHOD GET on loadPetWithVisit..... ");  
    	String url = URIConstants.PET_URL + "/owners/*/pets/{petId}";
    	Map<String, Integer> variables = new HashMap<String, Integer>(1);
        variables.put("petId", petId);    	
        Pet pet = restTemplate.getForObject(url, Pet.class, variables);
        PetVisitRequestBody obj = new PetVisitRequestBody();
        obj.setPet(pet);
        Visit visit = new Visit();
        pet.addVisit(visit);
        obj.setVisit(visit);
        return obj;
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @RequestMapping(value = "/owners/*/pets/*/visits/new", method = RequestMethod.POST)
    public Visit processNewVisitForm(@RequestBody Visit visit) {
    	System.out.println("SERVICE visit-controller.... METHOD POST on processNewVisitForm..... ");
    	System.out.println("VISIT ToString()....");
    	System.out.println(visit.getDescription());
    	Visit savedVisit = this.visits.save(visit);
    	return savedVisit;
    }
}
