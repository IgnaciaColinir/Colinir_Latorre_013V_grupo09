package ms.pagos.ms.pagos.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.pagos.ms.pagos.config.FeignConfig;
import ms.pagos.ms.pagos.dto.response.ConsultasResponse;

@FeignClient(
        name = "ms-consultas",
        url = "jdbc:mysql://mysql-db:3306/clinica_consultas?createDatabaseIfNotExist=true",
        configuration = FeignConfig.class
)
public interface ConsultaClient {

    // Llama al endpoint GET /api/v1/pacientes/{rut} del microservicio de pacientes
    @GetMapping("/api/v1/consulta/{id}")
    ConsultasResponse obtenerConsultaPorid(@PathVariable("id") int id);
}
