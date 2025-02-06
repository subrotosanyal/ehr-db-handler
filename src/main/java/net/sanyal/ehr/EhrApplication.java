package net.sanyal.ehr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(
    info = @Info(
        title = "EHR System API",
        version = "1.0",
        description = "API for Electronic Health Record (EHR) System"
    )
)
public class EhrApplication {
    public static void main(String[] args) {
        SpringApplication.run(EhrApplication.class, args);
    }
}
