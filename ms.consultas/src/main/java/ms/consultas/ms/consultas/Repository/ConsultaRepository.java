package ms.consultas.ms.consultas.Repository;

import org.springframework.stereotype.Repository;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository // Marca esta clase como repositorio (acceso a datos)
public class ConsultaRepository {

    private static final List<ModeloConsulta> consultasList = new ArrayList<>();
    private static int nextId = 1;

    public ConsultaRepository() {
        consultasList.add(ModeloConsulta.builder()
                .id(nextId++)
                .idPaciente("20753429-3")
                .nomPaciente("Isidora Contreras")
                .idMedico("20999255-8")
                .nomMedico("Fabian Navarro")
                .fechaConsulta(LocalDate.parse("2026-01-01"))
                .horaConsulta(LocalTime.parse("10:00"))
                .diagnostico("Chequeo médico")
                .valorConsulta(50000.0)
                .valorTratamiento(35000.0)
                .build());

        consultasList.add(ModeloConsulta.builder()
                .id(nextId++)
                .idPaciente("20753567-4")   
                .nomPaciente("Hans Pinilla")
                .idMedico("8669575-8")
                .nomMedico("Giovanni Roa")
                .fechaConsulta(LocalDate.parse("2026-03-02"))
                .horaConsulta(LocalTime.parse("11:00"))
                .diagnostico("Tratamiento especial")
                .valorConsulta(100000.)
                .valorTratamiento(50000.)
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

        for (int i = 0; i < consultasList.size(); i++) {
            ModeloConsulta c = consultasList.get(i);

            if (c != null && c.getId() == id) {

                c.setNomPaciente(consultaActualizada.getNomPaciente());
                c.setNomMedico(consultaActualizada.getNomMedico());
                c.setFechaConsulta(consultaActualizada.getFechaConsulta());
                c.setHoraConsulta(consultaActualizada.getHoraConsulta());
                c.setDiagnostico(consultaActualizada.getDiagnostico());
                c.setValorConsulta(consultaActualizada.getValorConsulta());
                c.setValorTratamiento(consultaActualizada.getValorTratamiento());

                return c;
            }
        }               
   
        return null;
    }

    public boolean deleteById(int id) {
        return consultasList.removeIf(p -> p != null && p.getId() == id); // Elimina el elemento si el id coincide, retorna true automaticamente


        
    }



    public List<ModeloConsulta> findByPaciente(String paciente) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p != null && p.getNomPaciente() != null && p.getNomPaciente().equalsIgnoreCase(paciente)) // Filtra por nombre de paciente ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }

    public List<ModeloConsulta> findByDate(String fechaConsulta) {
        LocalDate fechaBusqueda = LocalDate.parse(fechaConsulta);
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p != null && p.getFechaConsulta() != null && p.getFechaConsulta().isEqual(fechaBusqueda)) // Filtra por fecha de consulta ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }

    public List<ModeloConsulta> findByDiagnostico(String diagnostico) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p != null && p.getDiagnostico() != null && p.getDiagnostico().equalsIgnoreCase(diagnostico)) // Filtra por diagnostico ignorando mayúsculas/minúsculas
                .sorted(Comparator.comparing(ModeloConsulta::getId)) // Ordena por id
                .toList(); //recuerden siempre vovler a transformar el onjeto en una nueva lista
    }
    
    public List<ModeloConsulta> findByCancelada(String estado) {
        return consultasList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(c-> c != null) // Filtra solo los que no son nulos para evitar NullPointerException
                .filter(c-> c.getEstado() != null && c.getEstado().equalsIgnoreCase("cancelada")) // Filtra solo los que no están cancelados
                .toList(); //recuerden siempre vovler a transformar el objeto en una nueva lista
    }

   

    
}