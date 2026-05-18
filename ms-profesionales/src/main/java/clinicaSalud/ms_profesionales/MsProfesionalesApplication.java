package clinicaSalud.ms_profesionales;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;
import clinicaSalud.ms_profesionales.Repository.RepositoryProfesionales;

@SpringBootApplication
public class MsProfesionalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProfesionalesApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(RepositoryProfesionales repository) {
        return args -> {
            if (repository.count() == 0) {
                ModeloProfesional doc1 = ModeloProfesional.builder()
                        .rut("11111111-1")
                        .nombre("Mogul")
                        .apellido("Khan")
                        .especialidad("Medicina general")
                        .email("Khan@clinica.cl")
                        .telefono("+56912345678")
                        .valorConsulta(50000)
                        .build();
                repository.save(doc1);

                ModeloProfesional doc2 = ModeloProfesional.builder()
                        .rut("22222222-2")
                        .nombre("Jaimico")
                        .apellido("Cameron")
                        .especialidad("Inmunología")
                        .email("cameron@clinica.cl")
                        .telefono("+56987654321")
                        .valorConsulta(45000)
                        .build();
                repository.save(doc2);
            }
        };
    }
}