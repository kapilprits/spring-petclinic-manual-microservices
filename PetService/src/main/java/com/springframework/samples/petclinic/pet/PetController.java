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
package com.springframework.samples.petclinic.pet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class PetController {
    
	private RestTemplate restTemplate;	
	private final PetRepository pets;       
    
    @Autowired
    public PetController(PetRepository pets){//, OwnerRepository owners) {
        this.pets = pets;
        restTemplate = new RestTemplate();
    }
    
    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @RequestMapping(value = URIConstants.GET_PET_TYPES, method = RequestMethod.GET)
    @Transactional
    public PetType[] getPetTypes() {
    	Collection<PetType> results = pets.findPetTypes();
        PetType[] response = results.toArray(new PetType[results.size()]);
    	return response;
    }
    
    @RequestMapping(value = URIConstants.GET_OWNER_BYID + URIConstants.NEW_PET, method = RequestMethod.POST)
    public PetClinicRequestBody initCreationForm(@RequestBody Owner owner, @PathVariable("ownerId") int ownerId) {
    	System.out.println("SERVICE pet-controller.... METHOD POST on initCreationForm START..... ");
    	System.out.println("Owner name : " + owner.getFirstName() + owner.getLastName());    	    	    	
    	
    	Pet pet = new Pet();    
    	owner.addPet(pet);       	
    	PetClinicRequestBody obj = new PetClinicRequestBody();
    	obj.setOwner(owner);
    	obj.setPet(pet);
    	
    	/*String url = URIConstants.OWNER_URL + URIConstants.GET_OWNER_BYID + URIConstants.NEW_PET;
    	Map<String, Integer> variables = new HashMap<String, Integer>(1);
        variables.put("ownerId", ownerId);    	
        ResponseEntity<PetClinicRequestBody> returnedObj = restTemplate.postForEntity(url, obj, PetClinicRequestBody.class, variables);*/
        System.out.println(obj.getOwner().toString());
        System.out.println(obj.getPet().getOwner().toString());
    	System.out.println("SERVICE pet-controller.... METHOD POST on initCreationForm END..... ");
    	return obj;
    }
    
    @RequestMapping(value = URIConstants.GET_OWNER_BYID + URIConstants.CREATE_PET, method = RequestMethod.POST)
    @Transactional
    public PetClinicRequestBody processCreationForm(@RequestBody PetClinicRequestBody request, @PathVariable("ownerId") int ownerId) {
    	System.out.println("SERVICE pet-controller.... METHOD POST on processCreationForm....");   
    	String url = URIConstants.OWNER_URL + URIConstants.GET_OWNER_BYID + URIConstants.ADD_PET;
    	/*Map<String, Integer> variables = new HashMap<String, Integer>(1);
        variables.put("ownerId", ownerId);    	
        ResponseEntity<PetClinicRequestBody> returnedObj = restTemplate.postForEntity(url, request, PetClinicRequestBody.class, variables);
    	request = returnedObj.getBody();*/
    	request.getPet().setOwner(request.getOwner());
        Pet savedPet = pets.save(request.getPet());
        request.setPet(savedPet);
        return request;        
    }    
    
    @RequestMapping(value = URIConstants.GET_OWNER_BYID + URIConstants.EDIT_PET, method = RequestMethod.GET)
    @Transactional
    public Pet initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
        Pet pet = this.pets.findById(petId);
        return pet;        
    }
    
    @RequestMapping(value = URIConstants.GET_PET_BY_ID, method = RequestMethod.GET)
    @Transactional
    public Pet getPetById(@PathVariable("petId") int petId) {
    	System.out.println("SERVICE pet-controller.... METHOD GET on getPetById....");    	
    	Pet pet = this.pets.findById(petId);
        return pet;        
    }

    @RequestMapping(value = URIConstants.GET_OWNER_BYID + URIConstants.EDIT_PET, method = RequestMethod.POST)
    public PetClinicRequestBody processUpdateForm(PetClinicRequestBody request, @PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
        Pet pet = request.getPet();
    	pet.setOwner(request.getOwner());    	
        return request;        
    } 
}
