package clinicaSalud.ms_auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;

@SpringBootApplication
@EnableDiscoveryClient //  pa encender el eurika
public class MsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAuthApplication.class, args);
    }

    // Datos Hardcodeados exigidos por el profe
    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            // Solo creamos si la base está vacía
            if (repository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword("1234");
                admin.setRol("ADMIN");
                repository.save(admin);

                Usuario medico = new Usuario();
                medico.setUsername("doctor");
                medico.setPassword("salud123");
                medico.setRol("MEDICO");
                repository.save(medico);
            }
        };
    }
}