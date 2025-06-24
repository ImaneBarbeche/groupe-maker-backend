package com.group.groupemaker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class GroupeMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupeMakerApplication.class, args);
	}

	@Bean
public CommandLineRunner runner(ApplicationContext ctx) {
    return args -> {
        System.out.println(">>>> BEANS CHARGÉS :");
        Arrays.stream(ctx.getBeanDefinitionNames())
              .filter(name -> name.contains("formateur"))
              .forEach(System.out::println);
    };
}


}
