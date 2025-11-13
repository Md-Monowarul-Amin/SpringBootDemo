package com.example.springboot;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.EnableCaching;

import static com.example.springboot.utils.Constants.TEST_NAME;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {

        String[] myArgs = {
                "--test.name=test-name_xyz",
                "--another.prop=value",
                "--foo=bar"
        };


		SpringApplication.run(Application.class, myArgs);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

            generateVaultProperties();

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

    private static void generateVaultProperties() {

        System.setProperty(TEST_NAME, "test-name007");
    }


}
