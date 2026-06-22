package clinicaSalud.ms_fichas.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FichaDTO {
    private Long idFicha;
    
    @NotBlank(message = "El RUT del paciente es obligatorio")
    private String rutPaciente;
    
    @NotBlank(message = "El tipo de sangre es obligatorio")
    private String tipoSangre;
    
    private String alergias;
    
    @NotBlank(message = "Los antecedentes familiares son obligatorios")
    private String antecedentesFamiliares;

    private Object paciente;
}