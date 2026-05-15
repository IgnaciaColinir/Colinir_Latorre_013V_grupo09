package clinicaSalud.ms_fichas.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_fichas.Model.Ficha;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {
    // Para buscar la ficha usando el rut. Como apago el completador? 
    List<Ficha> findByRutPaciente(String rutPaciente);
}