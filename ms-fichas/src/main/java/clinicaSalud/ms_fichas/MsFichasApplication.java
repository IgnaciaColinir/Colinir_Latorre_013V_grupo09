package clinicaSalud.ms_fichas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient // Ojo: Agregué esto para Eureka
public class MsFichasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsFichasApplication.class, args);
    }
}