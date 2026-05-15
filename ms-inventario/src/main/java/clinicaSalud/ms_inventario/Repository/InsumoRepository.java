package clinicaSalud.ms_inventario.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_inventario.Model.Insumo;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    // Un regalito extra: buscar todos los insumos de una categoria especifica
    List<Insumo> findByCategoria(String categoria);
}