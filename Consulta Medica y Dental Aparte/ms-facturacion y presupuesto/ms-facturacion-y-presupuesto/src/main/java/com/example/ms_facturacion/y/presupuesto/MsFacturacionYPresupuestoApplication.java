package com.example.ms_facturacion.y.presupuesto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MsFacturacionYPresupuestoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsFacturacionYPresupuestoApplication.class, args);
    }
}
