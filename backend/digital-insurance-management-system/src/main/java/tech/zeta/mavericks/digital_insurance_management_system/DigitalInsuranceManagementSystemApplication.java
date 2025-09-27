package tech.zeta.mavericks.digital_insurance_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "tech.zeta.mavericks.digital_insurance_management_system.entity")

public class DigitalInsuranceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalInsuranceManagementSystemApplication.class, args);
	}
}
