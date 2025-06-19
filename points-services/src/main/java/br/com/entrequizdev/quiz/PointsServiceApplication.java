package br.com.entrequizdev.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.entrequizdev.quiz.auth")
public class PointsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PointsServiceApplication.class, args);
	}
}
