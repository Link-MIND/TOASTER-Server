package com.app.toaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class ToasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToasterApplication.class, args);
	}

}
