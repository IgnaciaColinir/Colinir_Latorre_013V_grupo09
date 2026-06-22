package clinicaSalud.ms_fichas.Repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import clinicaSalud.ms_fichas.Model.Ficha;

@Repository
public interface FichaRepository extends CrudRepository<Ficha, Long> {
    List<Ficha> findByRutPaciente(String rutPaciente);
}