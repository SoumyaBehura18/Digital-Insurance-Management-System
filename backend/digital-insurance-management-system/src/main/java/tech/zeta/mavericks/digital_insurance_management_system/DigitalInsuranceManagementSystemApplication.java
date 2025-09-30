package tech.zeta.mavericks.digital_insurance_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Entry point for the Digital Insurance Management System backend application.
 * This class starts the Spring Boot application and performs component scanning,
 * entity scanning, and configuration initialization.
 */
@SpringBootApplication
@EntityScan(basePackages = "tech.zeta.mavericks.digital_insurance_management_system.entity")
public class DigitalInsuranceManagementSystemApplication {

	/**
	 * Main method to launch the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DigitalInsuranceManagementSystemApplication.class, args);
	}
}
