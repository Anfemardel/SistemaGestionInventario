package com.example.inventario.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(classes = RestTemplateConfig.class)
class RestTemplateConfigTest {

    @Test
    void restTemplateBeanShouldBeCreated() {
        // Cargar el contexto con la configuración
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestTemplateConfig.class)) {
            // Obtener el bean del contexto
            RestTemplate restTemplate = context.getBean(RestTemplate.class);

            // Verificar que no sea nulo
            assertNotNull(restTemplate, "El bean RestTemplate debe existir");

            // Verificar tipo
            assertEquals(RestTemplate.class, restTemplate.getClass(), "Debe ser una instancia de RestTemplate");
        }
    }
}