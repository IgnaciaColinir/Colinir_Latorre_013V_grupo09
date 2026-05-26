package ms.consultas.ms.consultas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = "ms.consultas.ms.consultas")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
