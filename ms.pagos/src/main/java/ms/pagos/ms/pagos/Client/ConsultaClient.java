package ms.pagos.ms.pagos.Client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.pagos.ms.pagos.config.FeignConfig;
import ms.pagos.ms.pagos.dto.response.ConsultasResponse;

@FeignClient(
        name = "ms-consultas",
        url = "http://localhost:8098",      
        configuration = FeignConfig.class
)
public interface ConsultaClient {

    // Llama al endpoint GET /api/v1/consultas/{id} del microservicio de consultas
    @GetMapping("/api/v1/consultas/{id}")
    List<ConsultasResponse> obtenerConsultaPorid(@PathVariable("id") int id);

    @GetMapping("/api/v1/consultas")
    List<ConsultasResponse> obtenerTodos();
}
