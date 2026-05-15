package clinicaSalud.ms_fichas.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Ficha {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFicha;
    
    private String rutPaciente; // La clave para conectarlo mentalmente con el ms de la Nacha
    private String tipoSangre;
    private String alergias;
    private String antecedentesFamiliares;
}