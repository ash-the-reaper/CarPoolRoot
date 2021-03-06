package ashwin.uomtrust.ac.mu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages= "ashwin.uomtrust.ac.mu")
@EnableAsync
public class CarPoolBackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarPoolBackOfficeApplication.class, args);
	}
}
