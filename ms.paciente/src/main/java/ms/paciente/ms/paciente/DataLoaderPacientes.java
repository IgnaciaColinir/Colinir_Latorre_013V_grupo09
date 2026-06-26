package ms.paciente.ms.paciente;

import java.time.ZoneId;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Repository.RepositoryPacientes;
import net.datafaker.Faker;

//@Profile("dev")
//@Component
public class DataLoaderPacientes implements CommandLineRunner {
    @Autowired
    private RepositoryPacientes pacienteRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        String[] previsiones = {"FONASA", "ISAPRE", "PARTICULAR"};

        if (pacienteRepository.count() == 0) {
            for (int i = 0; i < 30; i++) {
                String rut = faker.number().numberBetween(10000000, 25000000) + "-" + faker.number().numberBetween(0, 9);
                
                ModeloPaciente paciente = ModeloPaciente.builder()
                        .rut(rut)
                        .nombre(faker.name().firstName())
                        .apellido(faker.name().lastName())
                        .direccion(faker.address().fullAddress())
                        .fechaNacimiento(faker.date().birthday(1, 90).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        .telefono(faker.phoneNumber().cellPhone())
                        .email(faker.internet().emailAddress())
                        .prevision(previsiones[faker.number().numberBetween(0, 3)])
                        .build();

                pacienteRepository.save(paciente);
            }
            System.out.println("=======> BD Pacientes poblada con 30 registros.");
        }
    }
}
