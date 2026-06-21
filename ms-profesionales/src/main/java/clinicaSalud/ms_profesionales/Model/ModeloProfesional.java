package clinicaSalud.ms_profesionales.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("profesionales") // MODO JDBC
public class ModeloProfesional {
    
    @Id
    private Long id; // Llave subrogada para JDBC

    private String rut; // Llave natural y única
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String telefono;
}