package com.springframework.samples.petclinic.pet;

public class URIConstants {
	public static final String OWNER_URL = "http://localhost:8094";
	public static final String ALL_OWNER = "/owners";
	public static final String NEW_OWNER = "/owners/new";
	public static final String FIND_OWNER = "/owners/find";
	public static final String EDIT_OWNER = "/owners/{ownerId}/edit";
	public static final String GET_OWNER_BYID = "/owners/{ownerId}";
	
	public static final String ADD_PET = "/pets/add";
	//public static final String GET_PET_BY_ID = "/petId";
	public static final String NEW_PET = "/pets/new";
	public static final String CREATE_PET = "/pets/create";	
	public static final String GET_PET_TYPES = "/petTypes";
	public static final String EDIT_PET = "/pets/{petId}/edit";	
	public static final String GET_PET_BY_ID = "/owners/*/pets/{petId}";
}
