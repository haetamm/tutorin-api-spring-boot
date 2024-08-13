package tutorin.com;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Tutorin API", version = "1.0.0", description = "Tutorin API"))
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class TutorinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorinApiApplication.class, args);
	}

}
