package clinicaSalud.ms_servicios;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;

@SpringBootApplication
public class MsServiciosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsServiciosApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ServicioRepository repository) {
        return args -> {
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
            }
        };
    }
}