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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
@RequestMapping(URIConstants.GET_OWNER_BYID)
class PetController {

	@Autowired
	private RestTemplate restTemplate;
	
	@ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
    	String url = URIConstants.PET_URL + URIConstants.GET_PET_TYPES;
    	ResponseEntity<PetType[]> responseEntity = restTemplate.getForEntity(url, PetType[].class); 
    	PetType[] results = responseEntity.getBody();     	
    	Collection<PetType> list = Arrays.asList(results); 
    	return list;
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int ownerId) {
    	String url = URIConstants.OWNER_URL + URIConstants.GET_OWNER_BYID;
    	Owner owner = restTemplate.getForObject(url, Owner.class, ownerId);
    	return owner;   	
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("pet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PetValidator());
    }
    
    @RequestMapping(value = URIConstants.NEW_PET, method = RequestMethod.GET)
    public String initCreationForm(Owner owner, @PathVariable int ownerId,  ModelMap model) throws Exception {
    	System.out.println("MAIN APP - PET CONTROLLER, NEW PET, GET, START....");
    	System.out.println("Owner name : " + owner.getFirstName() + owner.getLastName());
    	    	
    	String url = URIConstants.PET_URL + URIConstants.GET_OWNER_BYID + URIConstants.NEW_PET;
    	Map<String, Integer> variables = new HashMap<String, Integer>(1);
        variables.put("ownerId", ownerId);        
    	ResponseEntity<PetClinicRequestBody> response = restTemplate.postForEntity(url, owner, PetClinicRequestBody.class, variables);
    	
    	System.out.println("MAIN APP - PET CONTROLLER, NEW PET, GET, After post call....");
    	Pet pet = response.getBody().getPet();
    	if(pet.getOwner() == null)
    	{
    		pet.setOwner(owner);
    		System.out.println("Owner in pet is null");
    	}
    	else
    	{
    		System.out.println(pet.getOwner().toString());
    	}
    	owner = response.getBody().getOwner();
    	model.put("pet", pet);    	
    	System.out.println("MAIN APP - PET CONTROLLER, NEW PET, GET, END....");        
    	return URIConstants.CREATE_PET_HTML;
    }

    @RequestMapping(value = URIConstants.NEW_PET, method = RequestMethod.POST)
    public String processCreationForm(Owner owner, @Valid Pet pet, @PathVariable int ownerId, BindingResult result, ModelMap model) {
    	System.out.println("MAIN APP - PET CONTROLLER, NEW PET, POST, START....");    	
    	System.out.println("Pet name : " + pet.getName() + pet.getType() + pet.getBirthDate());
    	System.out.println(owner.toString());
        if(pet.getOwner() != null)
        {
        	System.out.println(pet.getOwner().toString());
        }
        else
        {
        	pet.setOwner(owner);
        	System.out.println("pet owner is null for adding new pet.");
        } 
    	if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
    	
    	if (result.hasErrors()) {
            model.put("pet", pet);
            System.out.println("MAIN APP - PET CONTROLLER, NEW PET, POST, ERROR END....");
            return URIConstants.CREATE_PET_HTML;
        } else {
        	String url = URIConstants.PET_URL + URIConstants.GET_OWNER_BYID + URIConstants.CREATE_PET;
        	PetClinicRequestBody obj = new PetClinicRequestBody();        	
        	obj.setOwner(owner);
        	obj.setPet(pet);
        	Map<String, Integer> variables = new HashMap<String, Integer>(1);
            variables.put("ownerId", ownerId);
        	
        	ResponseEntity<PetClinicRequestBody> response = restTemplate.postForEntity(url, obj, PetClinicRequestBody.class, variables);
            response.getBody().getPet().setOwner(response.getBody().getOwner());
            
        	System.out.println("MAIN APP - PET CONTROLLER, NEW PET, POST, END....");
            return "redirect:/owners/{ownerId}";
        }
    }

    @RequestMapping(value = URIConstants.EDIT_PET, method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, ModelMap model) {
    	System.out.println("MAIN APP - PET CONTROLLER, EDIT PET, GET, START....");    	
    	System.out.println("Pet Id for edit : " + petId);
    	System.out.println("Owner Id for edit : " + ownerId);
    	Map<String, Integer> variables = new HashMap<String, Integer>(2);
        variables.put("ownerId", ownerId);
        variables.put("petId", petId);
    	
    	String url = URIConstants.PET_URL + URIConstants.GET_OWNER_BYID + URIConstants.EDIT_PET;
    	Pet pet = restTemplate.getForObject(url, Pet.class, variables);
    	
    	String ownerurl = URIConstants.OWNER_URL + URIConstants.GET_OWNER_BYID;
    	Owner owner = restTemplate.getForObject(ownerurl, Owner.class, variables);
    	
    	pet.setOwner(owner);
    	
    	model.put("pet", pet);
    	
    	System.out.println("MAIN APP - PET CONTROLLER, EDIT PET, GET, END....");
    	return URIConstants.CREATE_PET_HTML;
    }

    @RequestMapping(value = URIConstants.EDIT_PET, method = RequestMethod.POST)
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, @PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, ModelMap model) {
    	System.out.println("MAIN APP - PET CONTROLLER, EDIT PET, POST, START....");
    	System.out.println("Owner Id for edit : " + ownerId);
    	System.out.println("Pet Id for edit : " + petId);
    	PetClinicRequestBody obj = new PetClinicRequestBody();
    	obj.setOwner(owner);
    	obj.setPet(pet);
    	
    	System.out.println("Pet Details for edit : " + pet.getName() + "  " + pet.getBirthDate());
    	
    	if (result.hasErrors()) {
    		Map<String, Integer> variables = new HashMap<String, Integer>(2);
            variables.put("ownerId", ownerId);
            variables.put("petId", petId);
        
    		String url = URIConstants.PET_URL + URIConstants.GET_OWNER_BYID + URIConstants.EDIT_PET;
        	
        	ResponseEntity<PetClinicRequestBody> response = restTemplate.postForEntity(url, obj, PetClinicRequestBody.class, variables);
        	response.getBody().getPet().setOwner(response.getBody().getOwner());
        	
        	model.put("pet", response.getBody().getPet());
        	System.out.println("MAIN APP - PET CONTROLLER, EDIT PET, POST, HASERRORS, END....");
            return URIConstants.CREATE_PET_HTML;
        } else {
        	Map<String, Integer> variables = new HashMap<String, Integer>(1);
            variables.put("ownerId", ownerId);
                    
        	String url = URIConstants.PET_URL + URIConstants.GET_OWNER_BYID + URIConstants.CREATE_PET;
        	
        	ResponseEntity<PetClinicRequestBody> response = restTemplate.postForEntity(url, obj, PetClinicRequestBody.class, variables);
        	response.getBody().getPet().setOwner(response.getBody().getOwner());
        	
        	System.out.println("MAIN APP - PET CONTROLLER, EDIT PET, POST, END....");
        	return "redirect:/owners/{ownerId}";
        }
    }
}
