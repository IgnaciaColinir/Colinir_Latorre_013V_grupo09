package ms.consultas.ms.consultas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Repository.ConsultaRepository;
import ms.consultas.ms.consultas.client.PacienteClient;
import ms.consultas.ms.consultas.client.ProfesionalClient;
import ms.consultas.ms.consultas.dto.response.PacienteResponse;
import ms.consultas.ms.consultas.dto.response.ProfesionalResponse;
import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoaderConsultas implements CommandLineRunner {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired(required = false) // Evita caídas si el microservicio de pacientes está apagado
    private PacienteClient pacienteClient;

    @Autowired(required = false) // 💡 AGREGADO: Evita caídas si ms-usuarios está apagado
    private ProfesionalClient profesionalClient;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        if (consultaRepository.count() == 0) {
            System.out.println("=======> [ms-consultas] Iniciando poblado...");

            // 1. Valores por defecto (Respaldos en caso de que los microservicios estén caídos)
            String rutPaciente = "12345678-9";
            String nomPaciente = "María Gómez";
            String rutMedico = "15999888-K";
            String nomMedico = "Dr. Alejandro Silva";

            // 2. 💡 LLAMADA FEIGN: Intentamos recuperar pacientes reales
            try {
                if (pacienteClient != null) {
                    List<PacienteResponse> pacientesExistentes = pacienteClient.obtenerTodos();
                    if (pacientesExistentes != null && !pacientesExistentes.isEmpty()) {
                        PacienteResponse p = pacientesExistentes.get(random.nextInt(pacientesExistentes.size()));
                        rutPaciente = p.getRut();
                        nomPaciente = p.getNombre() + " " + p.getApellido();
                    }
                }
            } catch (Exception e) {
                System.out.println("[ms-consultas] No se pudo conectar a ms-pacientes. Usando respaldo.");
            }

            try {
                if (profesionalClient != null) {
                    List<ProfesionalResponse> profesionalesExistentes = profesionalClient.obtenerTodos();
                    if (profesionalesExistentes != null && !profesionalesExistentes.isEmpty()) {
                        // Tomamos un profesional al azar de la base de datos de profesionales
                        ProfesionalResponse prof = profesionalesExistentes.get(random.nextInt(profesionalesExistentes.size()));
                        rutMedico = prof.getRut();
                        nomMedico = "Dr/Dra. " + prof.getNombre() + " " + prof.getApellido();
                    }
                }
            } catch (Exception e) {
                System.out.println("[ms-consultas] No se pudo conectar a ms-profesionales. Usando respaldo.");

            // 4. Generar las consultas utilizando la información cruzada
            for (int i = 0; i < 15; i++) {
                ModeloConsulta consulta = ModeloConsulta.builder()
                        .idPaciente(rutPaciente)
                        .nomPaciente(nomPaciente)
                        .idMedico(rutMedico)      
                        .nomMedico(nomMedico)    
                        .fechaConsulta(LocalDate.now().plusDays(faker.number().numberBetween(-10, 10)))
                        .horaConsulta(LocalTime.of(faker.number().numberBetween(8, 18), 0))
                        .diagnostico("Consulta: " + faker.lorem().sentence(3))
                        .valorConsulta(25000.0)
                        .valorTratamiento(faker.options().option(0.0, 5000.0, 12500.0))
                        .estado("FINALIZADA")
                        .build();

                consultaRepository.save(consulta);
            }
            System.out.println("=======> [ms-consultas] Base de datos poblada exitosamente amarrando Feign.");
        }
    }
}
}
