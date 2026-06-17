package clinicaSalud.ms_profesionales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;
import clinicaSalud.ms_profesionales.Repository.RepositoryProfesionales;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RepositoryProfesionales repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            ModeloProfesional doc1 = ModeloProfesional.builder()
                    .rut("11111111-1")
                    .nombre("Mogul")
                    .apellido("Khan")
                    .especialidad("Medicina general")
                    .email("Khan@clinica.cl")
                    .telefono("+56912345678")
                    .build();
            repository.save(doc1);

            ModeloProfesional doc2 = ModeloProfesional.builder()
                    .rut("22222222-2")
                    .nombre("Jaimico")
                    .apellido("Cameron")
                    .especialidad("Inmunología")
                    .email("cameron@clinica.cl")
                    .telefono("+56987654321")
                    .build();
            repository.save(doc2);
            
            log.info("Doctores cargados en el DataLoader.");
        }
    }
}