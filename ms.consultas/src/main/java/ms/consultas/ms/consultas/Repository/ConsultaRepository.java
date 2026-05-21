package ms.consultas.ms.consultas.Repository;

import org.springframework.stereotype.Repository;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository // Marca esta clase como repositorio (acceso a datos)
public class ConsultaRepository {

    private final List<ModeloConsulta> consultasList = new ArrayList<>();
    private int nextId = 1;

    public ConsultaRepository() {
        consultasList.add(ModeloConsulta.builder()
                .id(nextId++)
                .nomPaciente("Isidora Contreras")
                .nomMedico("Fabian Navarro")
                .fechaConsulta("2026-01-01")
                .horaConsulta("10:00")
                .diagnostico("Chequeo médico")
                .valorConsulta(50000)
                .valorTratamiento(35000)
                .build());

        consultasList.add(ModeloConsulta.builder()
                .id(nextId++)
                .nomPaciente("Hans Pinilla")
                .nomMedico("Giovanni Roa")
                .fechaConsulta("2026-03-02")
                .horaConsulta("11:00")
                .diagnostico("Tratamiento especial")
                .valorConsulta(100000)
                .valorTratamiento(50000)
                .build());
    }

    public List<ModeloConsulta> findAll() {
        return consultasList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id ascendente
                .toList(); // Convierte el stream nuevamente a lista
    }

    public List<ModeloConsulta> findById(int id) {
        return consultasList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getId() == id) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }

    public ModeloConsulta save(ModeloConsulta nuevaConsulta) {
        nuevaConsulta.setId(nextId++); // Genera id autoincremental en la consulta de entrada
        consultasList.add(nuevaConsulta);
        return nuevaConsulta;
    }

    public ModeloConsulta update(int id, ModeloConsulta consultaActualizada) {

        /* EJEMPLO 1
        Recorro toda la lista con un for normal.
        Si encuentro una consulta con el mismo id,
        actualizo sus datos y lo retorno.
        Si no lo encuentro, retorno null.
        */
        for (int i = 0; i < consultasList.size(); i++) {

            if (consultasList.get(i).getId() == id) {

                consultasList.get(i).setNomPaciente(consultaActualizada.getNomPaciente());
                consultasList.get(i).setNomMedico(consultaActualizada.getNomMedico());
                consultasList.get(i).setFechaConsulta(consultaActualizada.getFechaConsulta());
                consultasList.get(i).setHoraConsulta(consultaActualizada.getHoraConsulta());
                consultasList.get(i).setDiagnostico(consultaActualizada.getDiagnostico());
                consultasList.get(i).setValorConsulta(consultaActualizada.getValorConsulta());
                consultasList.get(i).setValorTratamiento(consultaActualizada.getValorTratamiento());

                return consultasList.get(i);
            }
        }               
   
        return null;
    }

    public boolean deleteById(int id) {
        return consultasList.removeIf(p -> p.getId() == id); // Elimina el elemento si el id coincide, retorna true automaticamente


        
    }


    public boolean deleteByPaciente(String paciente) {
        return consultasList.removeIf(p -> p.getNomPaciente().equalsIgnoreCase(paciente)); // Elimina el elemento si el nombre de paciente coincide, retorna true automaticamente


        
    }

    public List<ModeloConsulta> findByPaciente(String paciente) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p.getNomPaciente().equalsIgnoreCase(paciente)) // Filtra por nombre de paciente ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }

    public List<ModeloConsulta> findByDate(String fechaConsulta) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p.getFechaConsulta().equalsIgnoreCase(fechaConsulta)) // Filtra por fecha de consulta ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }

    public List<ModeloConsulta> findByDiagnostico(String diagnostico) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p.getDiagnostico().equalsIgnoreCase(diagnostico)) // Filtra por diagnostico ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }
    

    
}