package clinicaSalud.ms_fichas;

import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FichaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
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
            f2.setAntecedentesFamiliares("Paciente Tony Hawk: Fractura de fémur múltiple...");
            repository.save(f2);
            
            System.out.println("Fichas de prueba cargadas.");
        }
    }
}