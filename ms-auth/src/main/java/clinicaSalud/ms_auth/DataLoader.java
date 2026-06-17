package clinicaSalud.ms_auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
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
            
            log.info("Usuarios cargados exitosamente.");
        }
    }
}