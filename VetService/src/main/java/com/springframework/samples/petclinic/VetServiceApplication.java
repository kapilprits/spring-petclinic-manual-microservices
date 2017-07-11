package com.springframework.samples.petclinic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
@ComponentScan("com.springframework.samples.petclinic.vet")
@EntityScan("com.springframework.samples.petclinic.vet")
public class VetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetServiceApplication.class, args);
	}
	
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "OPTIONS", "POST", "PUT", "DELETE");
	} 
}
