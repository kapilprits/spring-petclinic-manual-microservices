package com.springframework.samples.petclinic.controllers;

public class URIConstants {
	public static final String OWNER_URL = "http://localhost:8094";
	public static final String ALL_OWNER = "/owners";
	public static final String NEW_OWNER = "/owners/new";
	public static final String FIND_OWNER = "/owners/find";
	public static final String EDIT_OWNER = "/owners/{ownerId}/edit";
	public static final String GET_OWNER_BYID = "/owners/{ownerId}";
	
	public static final String OWNER_LIST_HTML = "owners/ownersList";
	public static final String CREATE_OWNER_HTML = "owners/createOrUpdateOwnerForm";
	public static final String FIND_OWNER_HTML = "owners/findOwners";
	public static final String OWNER_DETAILS_HTML = "owners/ownerDetails";
	
	public static final String NEW_PET = "/pets/new";
	public static final String CREATE_PET = "/pets/create";	
	public static final String GET_PET_TYPES = "/petTypes";
	public static final String EDIT_PET = "/pets/{petId}/edit";	
	
	public static final String PET_URL = "http://localhost:8093";
	public static final String CREATE_PET_HTML = "pets/createOrUpdatePetForm";
	
	public static final String VET_URL = "http://localhost:8090";
	public static final String GET_VETS = "/vets";
	public static final String VET_HTML = "/vets.html";
	public static final String VET_JSON = "/vets.json";
	public static final String VET_XML = "/vets.xml";
	public static final String SHOW_VET_HTML = "vets/vetList";
	
	public static final String VISIT_URL = "http://localhost:8091";
	public static final String GET_PET_BY_ID = "/petId";
	public static final String CREATE_VISIT_HTML = "pets/createOrUpdateVisitForm";
	
}
