package banksys.sys;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "DJ Bank Application",
				description = "Backend Rest API for DJ Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Daksh Jain",
						email = "daksh.jain@castler.com"
				)
		)
)
public class SysApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysApplication.class, args);
	}

}
