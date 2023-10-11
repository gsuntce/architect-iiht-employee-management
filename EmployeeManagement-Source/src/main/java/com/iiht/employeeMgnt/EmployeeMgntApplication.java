package com.iiht.employeeMgnt;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class EmployeeMgntApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeMgntApplication.class, args);
	}

	@Configuration
	public class SpringDocOpenApiConfig {

		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(getInfo());
		}

		private Info getInfo() {
			return new Info()
					.title("Employee REST API")
					.description("Employee API REST calls using Spring boot")
					.version("1.0")
					.termsOfService("Terms of service")
					.license(new License().name("License of API").url("API license URL"));
		}
	}

}
