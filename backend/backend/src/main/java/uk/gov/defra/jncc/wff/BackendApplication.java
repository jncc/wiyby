package uk.gov.defra.jncc.wff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EntityScan(basePackages = {"uk.gov.defra.jncc.wff.crud.entity"})
@EnableJpaRepositories(basePackages = {"uk.gov.defra.jncc.wff.crud.repository"})
@EnableSpringDataWebSupport
@Profile("default")
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
