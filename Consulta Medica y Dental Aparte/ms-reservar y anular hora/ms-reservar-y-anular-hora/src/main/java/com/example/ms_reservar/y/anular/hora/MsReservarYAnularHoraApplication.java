package com.example.ms_reservar.y.anular.hora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MsReservarYAnularHoraApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsReservarYAnularHoraApplication.class, args);
    }
}
