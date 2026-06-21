package ms.pagos.ms.pagos.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.pagos.ms.pagos.Modelo.ModeloPago;

@Repository
public interface RepositoryPago extends JpaRepository <ModeloPago, Integer>{

    List<ModeloPago> findByIdPaciente(String idPaciente);

    List<ModeloPago> findByIdConsulta(int idConsulta);

    
} 