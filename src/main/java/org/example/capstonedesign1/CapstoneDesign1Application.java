package org.example.capstonedesign1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CapstoneDesign1Application {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneDesign1Application.class, args);
    }

}
