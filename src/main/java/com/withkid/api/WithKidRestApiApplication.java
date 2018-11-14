package com.withkid.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
        basePackageClasses = {WithKidRestApiApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class WithKidRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithKidRestApiApplication.class, args);
	}
}
