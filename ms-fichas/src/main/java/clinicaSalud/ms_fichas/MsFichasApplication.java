package clinicaSalud.ms_fichas;

import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableFeignClients // ¡ESTO ES CLAVE PARA CONECTAR LOS MICROSERVICIOS DESPUÉS!
public class MsFichasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsFichasApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(FichaRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Paciente normal
                Ficha f1 = new Ficha();
                f1.setRutPaciente("19876543-2");
                f1.setTipoSangre("O+");
                f1.setAlergias("Penicilina");
                f1.setAntecedentesFamiliares("Hipertensión");
                repository.save(f1);

                
                Ficha f2 = new Ficha();
                f2.setRutPaciente("00000900-T"); 
                f2.setTipoSangre("A-");
                f2.setAlergias("Ibuprofeno");
                f2.setAntecedentesFamiliares("Paciente Tony Hawk: Fractura de fémur múltiple, contusiones por caída en rampa realizando el salto 900. Requiere pabellón urgente.");
                repository.save(f2);
            }
        };
    }
}