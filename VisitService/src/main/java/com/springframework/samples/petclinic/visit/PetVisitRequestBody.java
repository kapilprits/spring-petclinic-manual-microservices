package com.springframework.samples.petclinic.visit;

import java.io.Serializable;

public class PetVisitRequestBody implements Serializable {
private Visit visit;
private Pet pet;
public Visit getVisit() {
	return visit;
}
public void setVisit(Visit visit) {
	this.visit = visit;
}
public Pet getPet() {
	return pet;
}
public void setPet(Pet pet) {
	this.pet = pet;
}
}
