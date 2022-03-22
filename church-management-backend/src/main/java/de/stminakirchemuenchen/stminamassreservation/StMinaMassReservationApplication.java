package de.stminakirchemuenchen.stminamassreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StMinaMassReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StMinaMassReservationApplication.class, args);
	}

}
