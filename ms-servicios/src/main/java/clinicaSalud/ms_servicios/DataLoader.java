package clinicaSalud.ms_servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ServicioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Servicio s1 = new Servicio();
            s1.setNombre("Hemograma Completo");
            s1.setDescripcion("Análisis de sangre para medir glóbulos rojos, blancos y plaquetas.");
            s1.setPrecio(15000);
            s1.setRequiereAyuno(true);
            repository.save(s1);

            Servicio s2 = new Servicio();
            s2.setNombre("Consulta Medicina General");
            s2.setDescripcion("Evaluación médica de rutina con médico general.");
            s2.setPrecio(35000);
            s2.setRequiereAyuno(false);
            repository.save(s2);
            
            log.info("Catálogo de servicios cargado exitosamente.");
        }
    }
}