package com.example.ms_opinion.del.paciente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MsOpinionDelPacienteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsOpinionDelPacienteApplication.class, args);
    }
}
