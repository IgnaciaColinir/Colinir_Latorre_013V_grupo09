package ms.pagos.ms.pagos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ms.pagos.ms.pagos.Client.ConsultaClient;
import ms.pagos.ms.pagos.Client.PacienteClient;
import ms.pagos.ms.pagos.Modelo.ModeloPago;
import ms.pagos.ms.pagos.Repository.RepositoryPago;
import ms.pagos.ms.pagos.dto.response.ConsultasResponse;
import ms.pagos.ms.pagos.dto.response.PacienteResponse;
import net.datafaker.Faker;

//@Profile("dev")
//@Component
public class DataLoaderPagos implements CommandLineRunner{
    @Autowired
    private RepositoryPago pagoRepository;

    @Autowired(required = false)
    private ConsultaClient consultaClient;

    @Autowired(required = false)
    private PacienteClient pacienteClient; 
    
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        String[] metodosPago = {"EFECTIVO", "DEBITO", "CREDITO", "TRANSFERENCIA"};
        String[] estadosPago = {"PENDIENTE", "PAGADO", "REEMBOLSADO"};

        if (pagoRepository.count() == 0) {
            System.out.println("=======> [ms-pagos] Iniciando poblado cruzando Consultas y Pacientes...");

            // Respaldos por si los servicios están apagados
            int idConsultaRespaldo = 1;
            String idPacienteRespaldo = "12345678-9";
            double valorConsultaRespaldo = 25000.0;
            double valorTratamientoRespaldo = 5000.0;

            List<ConsultasResponse> consultasExistentes = null;
            List<PacienteResponse> pacientesExistentes = null;

            // 1. Intentamos recuperar consultas reales
            try {
                if (consultaClient != null) {
                    consultasExistentes = consultaClient.obtenerTodos();
                }
            } catch (Exception e) {
                System.out.println(" [ms-pagos] Sin conexión a ms-consultas.");
            }

            // 2. Intentamos recuperar pacientes reales para extraer sus RUTs
            try {
                if (pacienteClient != null) {
                    pacientesExistentes = pacienteClient.obtenerTodos();
                }
            } catch (Exception e) {
                System.out.println(" [ms-pagos] Sin conexión a ms-pacientes.");
            }

            boolean usarDatosReales = (consultasExistentes != null && !consultasExistentes.isEmpty());

            // 3. Poblado de la tabla de pagos
            for (int i = 0; i < 15; i++) {
                int idConsulta;
                String idPaciente = idPacienteRespaldo; 
                double valorConsulta;
                double valorTratamiento;

                if (usarDatosReales) {
                    ConsultasResponse consulta = consultasExistentes.get(random.nextInt(consultasExistentes.size()));
                    idConsulta = consulta.getId();
                    valorConsulta = consulta.getValorConsulta();
                    valorTratamiento = consulta.getValorTratamiento();

                    // 💡 LÓGICA DE CRUCE: Buscamos si el paciente existe en la lista para sacarle el RUT real
                    if (pacientesExistentes != null && !pacientesExistentes.isEmpty()) {
                        String nombreCompletoConsulta = consulta.getNomPaciente();
                        
                        // Intentamos buscar coincidencia por nombre
                        PacienteResponse pacienteEncontrado = pacientesExistentes.stream()
                                .filter(p -> nombreCompletoConsulta.contains(p.getNombre()) || nombreCompletoConsulta.contains(p.getApellido()))
                                .findFirst()
                                .orElse(pacientesExistentes.get(random.nextInt(pacientesExistentes.size()))); // Si no coincide, toma uno al azar de la base de datos
                        
                        idPaciente = pacienteEncontrado.getRut();
                    }
                } else {
                    idConsulta = idConsultaRespaldo + i;
                    valorConsulta = valorConsultaRespaldo;
                    valorTratamiento = valorTratamientoRespaldo;
                }

                double montoTotal = valorConsulta + valorTratamiento;
                String metodoAleatorio = metodosPago[random.nextInt(metodosPago.length)];
                String estadoAleatorio = estadosPago[random.nextInt(estadosPago.length)];

                ModeloPago pago = ModeloPago.builder()
                        .idConsulta(idConsulta)
                        .idPaciente(idPaciente) 
                        .valorConsulta(valorConsulta)
                        .valorTratamiento(valorTratamiento)
                        .montoTotal(montoTotal)
                        .metodoPago(metodoAleatorio)
                        .estado(estadoAleatorio)
                        .fechaPago(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 5)))
                        .build();

                pagoRepository.save(pago);
            }

            System.out.println("=======> [ms-pagos] ¡Poblado exitoso! Datos de pagos amarrados correctamente con Consultas y Pacientes.");
        }
    }

}
