package com.springframework.samples.petclinic.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PetClinicRequestBody implements Serializable {
private Owner owner;
private Pet pet;
public Owner getOwner() {
	return owner;
}
public void setOwner(Owner owner) {
	this.owner = owner;
}
public Pet getPet() {
	return pet;
}
public void setPet(Pet pet) {
	this.pet = pet;
}
}
