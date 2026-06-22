package clinicaSalud.ms_inventario.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("insumo") // Puro JDBC
public class Insumo {
    @Id
    private Long idInsumo;
    private String nombre;
    private String categoria; 
    private int stockActual;
    private int stockMinimo; 
}