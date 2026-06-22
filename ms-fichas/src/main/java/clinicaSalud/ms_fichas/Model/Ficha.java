package clinicaSalud.ms_fichas.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("ficha") // Modo JDBC, cero JPA
public class Ficha {
    @Id
    private Long idFicha;
    private String rutPaciente;
    private String tipoSangre;
    private String alergias;
    private String antecedentesFamiliares;
}