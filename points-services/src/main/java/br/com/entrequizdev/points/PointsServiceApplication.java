package br.com.entrequizdev.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.entrequizdev.points.auth")
public class PointsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PointsServiceApplication.class, args);
	}
}
