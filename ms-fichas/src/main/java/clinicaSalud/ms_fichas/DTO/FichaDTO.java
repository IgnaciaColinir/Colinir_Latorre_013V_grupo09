package clinicaSalud.ms_fichas.DTO;

import lombok.Data;

@Data
public class FichaDTO {
    private Long idFicha;
    private String rutPaciente;
    private String tipoSangre;
    private String alergias;
    private String antecedentesFamiliares;

    // Agrega esto a tus otras variables en FichaDTO.java
private Object paciente;

public Object getPaciente() {
    return paciente;
}

public void setPaciente(Object paciente) {
    this.paciente = paciente;
}
}

