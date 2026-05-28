package ms.agenda.agenda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ms.agenda.agenda.confg.FeignConfig;
import ms.agenda.agenda.dto.response.PacienteResponse;

// Cliente Feign para consumir el microservicio de pacientes
@FeignClient(
        name = "ms-paciente",
        url = "http://localhost:8095/api/v1/pacientes",
        configuration = FeignConfig.class
)
public interface PacienteClient {

    // Llama al endpoint GET /api/v1/pacientes/rut/{rut} del microservicio de pacientes
    @GetMapping("/rut/{rut}")
    PacienteResponse obtenerPacientePorRut(@PathVariable("rut") String rut);
}