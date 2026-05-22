package ms.pagos.ms.pagos.Repository;


import ms.pagos.ms.pagos.Modelo.ModeloPago;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository // Marca esta clase como repositorio (acceso a datos)
public class RepositoryPago {

    private final List<ModeloPago> pagoList = new ArrayList<>();
    private int nextId = 1;

    public RepositoryPago() {
        pagoList.add(ModeloPago.builder()
                .id(nextId++)
                .idConsulta(1)
                .idPaciente("P001")
                .valorConsulta(20000)
                .valorTratamiento(15000)
                .montoTotal(35000)
                .metodoPago("Tarjeta")
                .estado("Pagado")
                .fechaPago(LocalDateTime.now())
                .build());
    }


    public List<ModeloPago> findAll() {
        return pagoList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModeloPago::getId)) // Ordena por id ascendente
                .toList(); // Convierte el stream nuevamente a lista
    }

    public List<ModeloPago> findById(int id) {
        return pagoList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getId() == id) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }

    public ModeloPago save(ModeloPago nuevoPago) {
        nuevoPago.setId(nextId++); // Genera id autoincremental en el pago de entrada
        pagoList.add(nuevoPago);
        return nuevoPago;
    }

    public ModeloPago update(int id, ModeloPago pagoActualizado) {

        
        for (int i = 0; i < pagoList.size(); i++) {

            if (pagoList.get(i).getId() == id) {

                pagoList.get(i).setIdConsulta(pagoActualizado.getIdConsulta());
                pagoList.get(i).setIdPaciente(pagoActualizado.getIdPaciente());
                pagoList.get(i).setValorConsulta(pagoActualizado.getValorConsulta());
                pagoList.get(i).setValorTratamiento(pagoActualizado.getValorTratamiento());
                pagoList.get(i).setMontoTotal(pagoActualizado.getMontoTotal());
                pagoList.get(i).setMetodoPago(pagoActualizado.getMetodoPago());
                pagoList.get(i).setEstado(pagoActualizado.getEstado());
                pagoList.get(i).setFechaPago(pagoActualizado.getFechaPago());

                return pagoList.get(i);
            }
        }               


        
        return null;
    }

    public boolean deleteById(int id) {
        return pagoList.removeIf(p -> p.getId() == id); // Elimina el elemento si el id coincide, retorna true automaticamente

    }

    public List<ModeloPago> findByConsulta(int idconsulta) {
        return pagoList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p.getIdConsulta() == idconsulta) // Filtra por tipo ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloPago::getId)) // Ordena por id
                .toList(); // Retorna el primer elemento que coincida o un Optional vacío si no encuentra nada
    }

    
}