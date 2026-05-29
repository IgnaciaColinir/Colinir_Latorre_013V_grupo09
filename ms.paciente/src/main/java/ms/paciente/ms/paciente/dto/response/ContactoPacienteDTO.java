package ms.paciente.ms.paciente.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactoPacienteDTO {
    private String telefono;
    private String email;

}
