package ms.agenda.agenda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ms.agenda.agenda.confg.FeignConfig;
import ms.agenda.agenda.dto.response.PacienteResponse;

// Cliente Feign para consumir el microservicio de pacientes
@FeignClient(
        name = "ms-paciente",
        url = "jdbc:mysql://mysql-db:3306/clinica_pacientes?createDatabaseIfNotExist=true",
        configuration = FeignConfig.class
)
public interface PacienteClient {

    // Llama al endpoint GET /api/v1/pacientes/{id} del microservicio de pacientes
    @GetMapping("/api/v1/pacientes/{rut}")
    PacienteResponse obtenerPacientePorRut(@PathVariable("rut") String rut);
}