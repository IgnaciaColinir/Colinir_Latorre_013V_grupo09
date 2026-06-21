package ms.consultas.ms.consultas.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.consultas.ms.consultas.config.FeignConfig;
import ms.consultas.ms.consultas.dto.response.PacienteResponse;

// Cliente Feign para consumir el microservicio de pacientes
@FeignClient(
        name = "ms-paciente",
        url = "http://localhost:8095",
        configuration = FeignConfig.class)
public interface PacienteClient {

    // Llama al endpoint GET /api/v1/pacientes/rut/{rut} del microservicio de pacientes
    @GetMapping("/api/v1/pacientes/rut/{rut}")
    PacienteResponse obtenerPacientePorRut(@PathVariable("rut") String rut);

    @GetMapping("/api/v1/pacientes")
    List<PacienteResponse> obtenerTodos();
}