package clinicaSalud.ms_fichas.DTO;

import lombok.Data;

@Data
public class FichaDTO {
    private Long idFicha;
    private String rutPaciente;
    private String tipoSangre;
    private String alergias;
    private String antecedentesFamiliares;
}