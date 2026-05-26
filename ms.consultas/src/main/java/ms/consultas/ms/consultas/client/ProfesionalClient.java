package ms.consultas.ms.consultas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.consultas.ms.consultas.config.FeignConfig;
import ms.consultas.ms.consultas.dto.response.ProfesionalResponse;

@FeignClient(
        name = "ms-profesionales",
        url = "http://localhost:8092",
        configuration = FeignConfig.class
)
public interface ProfesionalClient {

    // Llama al endpoint GET /api/v1/profesionales/rut/{rut} del microservicio de profesionales
    @GetMapping("/api/v1/profesionales/rut/{rut}")
    ProfesionalResponse obtenerProfesionalPorRut(@PathVariable("rut") String rut);

}
