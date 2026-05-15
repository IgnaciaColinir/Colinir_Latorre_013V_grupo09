package clinicaSalud.ms_servicios;

import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsServiciosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsServiciosApplication.class, args);
	}

    // Este bloque inyecta datos directo a la base cuando le das Play
	@Bean
	CommandLineRunner initDatabase(ServicioRepository repository) {
		return args -> {
			Servicio s1 = new Servicio();
			s1.setNombre("Hemograma Completo");
			s1.setPrecio(15000);
			repository.save(s1);

			Servicio s2 = new Servicio();
			s2.setNombre("Consulta Medicina General");
			s2.setPrecio(35000);
			repository.save(s2);
		};
	}
}