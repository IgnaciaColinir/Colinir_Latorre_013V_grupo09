package clinicaSalud.ms_fichas.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Aquí le decimos que llame al ms-paciente en su puerto 8095
@FeignClient(name = "ms-paciente", url = "http://localhost:8095/api/v1/pacientes")
public interface PacienteFeignClient {

    
    @GetMapping("/{rut}")
    Object obtenerPacientePorRut(@PathVariable("rut") String rut);
}