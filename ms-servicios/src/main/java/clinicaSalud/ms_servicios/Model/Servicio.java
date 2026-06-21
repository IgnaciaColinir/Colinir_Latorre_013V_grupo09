package clinicaSalud.ms_servicios.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("servicio") // MODO JDBC
public class Servicio {
    
    @Id
    private Long idServicio;
    
    private String nombre; 
    private String descripcion;
    private int precio; 
    private boolean requiereAyuno; 
}