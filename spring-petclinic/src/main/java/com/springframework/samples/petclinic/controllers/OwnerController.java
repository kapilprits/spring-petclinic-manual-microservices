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


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.client.RestTemplate;

import com.springframework.samples.petclinic.models.Owner;


/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {

	@Autowired
	private RestTemplate restTemplate;
	
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping(method = RequestMethod.GET, value = URIConstants.NEW_OWNER)
    public String initCreationForm(Map<String, Object> model) throws IOException {
    	String url = URIConstants.OWNER_URL + URIConstants.NEW_OWNER;
    	Owner owner = restTemplate.getForObject(url, Owner.class);
    	model.put("owner", owner);    	
    	return URIConstants.CREATE_OWNER_HTML;        
    }

    @RequestMapping(method = RequestMethod.POST, value = URIConstants.NEW_OWNER)
    public String processCreationForm(@Valid Owner owner, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return URIConstants.CREATE_OWNER_HTML;
        } else {
        	String url = URIConstants.OWNER_URL + URIConstants.NEW_OWNER;
        	System.out.println("Main app owner-controller.... METHOD POST on creating owner....");    
        	System.out.println(owner.toString());
        	ResponseEntity<Owner> response = restTemplate.postForEntity(url, owner, Owner.class);
        	return "redirect:/owners/" + response.getBody().getId();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = URIConstants.FIND_OWNER)
    public String initFindForm(Map<String, Object> model) {
    	/*model.put("owner", new Owner());
        return "owners/findOwners";*/
    	String url = URIConstants.OWNER_URL + URIConstants.FIND_OWNER;
    	Owner owner = restTemplate.getForObject(url, Owner.class);
    	model.put("owner", owner);
        return URIConstants.FIND_OWNER_HTML;
    }

    @RequestMapping(method = RequestMethod.GET, value = URIConstants.ALL_OWNER)
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

        // find owners by last name
    	String url = URIConstants.OWNER_URL + URIConstants.ALL_OWNER;
    	ResponseEntity<Owner[]> responseEntity = restTemplate.postForEntity(url, owner, Owner[].class); 
    	Owner[] results = responseEntity.getBody();
    	//Collection<Owner> results = restTemplate.getForObject(OwnerRestURIConstants.OWNER_URL + "/", ArrayList.class);    	 
        if (results.length == 0) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return URIConstants.FIND_OWNER_HTML;
        } else if (results.length == 1) {
            // 1 owner found
            owner = results[0];
            return "redirect:/owners/" + owner.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return URIConstants.OWNER_LIST_HTML;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = URIConstants.EDIT_OWNER)
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
    	String url = URIConstants.OWNER_URL + URIConstants.EDIT_OWNER;
    	Owner owner = restTemplate.getForObject(url, Owner.class, ownerId);
    	model.addAttribute(owner);
        return URIConstants.CREATE_OWNER_HTML;
    }

    @RequestMapping(method = RequestMethod.POST, value = URIConstants.EDIT_OWNER)
    public String processUpdateOwnerForm(Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return URIConstants.CREATE_OWNER_HTML;
        } else {            
        	String url = URIConstants.OWNER_URL + URIConstants.EDIT_OWNER;
        	owner.setId(ownerId);
        	Map<String, Integer> variables = new HashMap<String, Integer>(1);
            variables.put("ownerId", ownerId);
        	ResponseEntity<Owner> response = restTemplate.postForEntity(url, owner, Owner.class, variables);
            return "redirect:/owners/" + response.getBody().getId();            
        }
    }

    @RequestMapping(value = URIConstants.GET_OWNER_BYID)
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
    	String url = URIConstants.OWNER_URL + URIConstants.GET_OWNER_BYID;
    	Owner owner = restTemplate.getForObject(url, Owner.class, ownerId);
    	ModelAndView mav = new ModelAndView(URIConstants.OWNER_DETAILS_HTML);
        mav.addObject(owner);
        return mav;
    }
}
