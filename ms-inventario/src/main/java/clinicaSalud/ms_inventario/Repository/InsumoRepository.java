package clinicaSalud.ms_inventario.Repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import clinicaSalud.ms_inventario.Model.Insumo;

@Repository
public interface InsumoRepository extends ListCrudRepository<Insumo, Long> {
    List<Insumo> findByCategoria(String categoria);
}