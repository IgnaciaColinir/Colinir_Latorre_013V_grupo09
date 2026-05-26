package ms.agenda.agenda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ms.agenda.agenda.confg.FeignConfig;
import ms.agenda.agenda.dto.response.ProfesionalResponse;

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
