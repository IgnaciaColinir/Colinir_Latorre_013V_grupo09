package ms.agenda.agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Repository.RepositoryAgenda;
import ms.agenda.agenda.client.PacienteClient;
import ms.agenda.agenda.client.ProfesionalClient;
import ms.agenda.agenda.dto.response.PacienteResponse;
import ms.agenda.agenda.dto.response.ProfesionalResponse;
import net.datafaker.Faker;

//@Profile("dev")
//@Component
public class DataLoaderAgenda implements CommandLineRunner {
    @Autowired
    private RepositoryAgenda agendaRepository;

    @Autowired(required = false)
    private PacienteClient pacienteClient;

    @Autowired(required = false)
    private ProfesionalClient profesionalClient;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Estados válidos según las anotaciones @Schema de tu modelo
        String[] estadosAgenda = {"RESERVADA", "CONFIRMADA", "CANCELADA", "ATENDIDA"};

        if (agendaRepository.count() == 0) {
            System.out.println("=======> [ms-agenda] Iniciando poblado de citas médicas...");

            String rutPacienteValido = "22222222-2";
            String rutProfesionalValido = "11111111-1";

            try {
                if (pacienteClient != null) {
                    List<PacienteResponse> pacientes = pacienteClient.obtenerTodos();
                    if (pacientes != null && !pacientes.isEmpty()) {
                        rutPacienteValido = pacientes.get(random.nextInt(pacientes.size())).getRut();
                    }
                }
            } catch (Exception e) {
                System.out.println(" [ms-agenda] No se pudo conectar a ms-pacientes. Usando respaldo.");
            }

            try {
                if (profesionalClient != null) {
                    List<ProfesionalResponse> profesionales = profesionalClient.obtenerTodos();
                    if (profesionales != null && !profesionales.isEmpty()) {
                        rutProfesionalValido = profesionales.get(random.nextInt(profesionales.size())).getRut();
                    }
                }
            } catch (Exception e) {
                System.out.println(" [ms-agenda] No se pudo conectar a ms-profesionales. Usando respaldo.");
            }

            for (int i = 0; i < 20; i++) {
                String estadoAleatorio = estadosAgenda[random.nextInt(estadosAgenda.length)];

                ModelAgenda cita = ModelAgenda.builder()
                        .fecha(LocalDate.now().plusDays(faker.number().numberBetween(1, 15)))
                        .hora(LocalTime.of(faker.number().numberBetween(8, 17), 30)) // Bloques ej: 09:30, 14:30
                        .idProfesional(rutProfesionalValido)
                        .idPaciente(rutPacienteValido)
                        .estado(estadoAleatorio)
                        .build();

                agendaRepository.save(cita);
            }
            System.out.println("=======> [ms-agenda] ¡Poblado completado con éxito! 20 citas listas en MySQL.");
        }
    }

}
