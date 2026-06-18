package com.example.ms_ficha.medica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MsFichaMedicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFichaMedicaApplication.class, args);
	}

}
