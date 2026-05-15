package clinicaSalud.ms_inventario.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Insumo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;
    
    private String nombre;
    private String categoria; // Ej: Insumo Medico, Medicamento, Aseo
    private int stockActual;
    private int stockMinimo; // Para saber cuando hay que comprar mas
}