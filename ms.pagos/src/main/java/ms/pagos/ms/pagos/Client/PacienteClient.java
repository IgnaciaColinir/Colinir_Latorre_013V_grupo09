package ms.pagos.ms.pagos.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.pagos.ms.pagos.config.FeignConfig;
import ms.pagos.ms.pagos.dto.response.PacienteResponse;


// Cliente Feign para consumir el microservicio de pacientes
@FeignClient(
        name = "ms-paciente",
        url = "jdbc:mysql://mysql-db:3306/clinica_pacientes?createDatabaseIfNotExist=true",
        configuration = FeignConfig.class
)
public interface PacienteClient {

    // Llama al endpoint GET /api/v1/pacientes/{rut} del microservicio de pacientes
    @GetMapping("/api/v1/pacientes/{rut}")
    PacienteResponse obtenerPacientePorRut(@PathVariable("rut") String rut);
}