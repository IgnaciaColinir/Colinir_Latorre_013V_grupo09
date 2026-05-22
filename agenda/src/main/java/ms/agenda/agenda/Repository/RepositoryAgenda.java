package ms.agenda.agenda.Repository;

import org.springframework.stereotype.Repository;

import ms.agenda.agenda.Model.ModelAgenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository // Marca esta clase como repositorio (acceso a datos)
public class RepositoryAgenda {

    private final List<ModelAgenda> agendaList = new ArrayList<>();
    private int nextId = 1;

    public RepositoryAgenda() {
        agendaList.add(ModelAgenda.builder()
                .id(nextId++)
                .fecha(LocalDate.parse("14/02/2026"))
                .hora(LocalTime.parse("10:00"))
                .idProfesional("1")
                .idPaciente("Ash")
                .estado("reservada")
                .build());

        agendaList.add(ModelAgenda.builder()
                .id(nextId++)
                .fecha(LocalDate.parse("15/02/2026"))
                .hora(LocalTime.parse("11:00"))
                .idProfesional("2")
                .idPaciente("Misty")
                .estado("cancelada")
                .build());
    }

  


    public List<ModelAgenda> findAll() {
        return agendaList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModelAgenda::getId)) // Ordena por id ascendente
                .toList(); // Convierte el stream nuevamente a lista
    }


    public ModelAgenda save(ModelAgenda nuevaCita) {
        nuevaCita.setId(nextId++); // Genera id autoincremental en el pokemon de entrada
        agendaList.add(nuevaCita);
        return nuevaCita;
    }

    public List<ModelAgenda> findById(int id) {
        return agendaList.stream() // Stream para recorrer la colección 
                .filter(c -> c.getId() == id) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }

    public List<ModelAgenda> findByIdPaciente(String idPaciente) {
        return agendaList.stream() // Stream para recorrer la colección 
                .filter(c -> c.getIdPaciente().equalsIgnoreCase(idPaciente)) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }

    public ModelAgenda update(int id, ModelAgenda citaActualizada) {

        for (int i = 0; i < agendaList.size(); i++) {

            if (agendaList.get(i).getId() == id) {

                agendaList.get(i).setFecha(citaActualizada.getFecha());
                agendaList.get(i).setHora(citaActualizada.getHora());
                agendaList.get(i).setIdProfesional(citaActualizada.getIdProfesional());
                agendaList.get(i).setIdPaciente(citaActualizada.getIdPaciente());
                agendaList.get(i).setEstado(citaActualizada.getEstado());

                return agendaList.get(i);
            }
        }               


 
        return null;
    }

    public boolean deleteById(int id) {
     
        return agendaList.removeIf(p -> p.getId() == id); 

    }

    public List<ModelAgenda> findByProfesionalAndFecha(String idProfesional, LocalDate fecha) {
        return agendaList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(c -> c.getIdProfesional().equals(idProfesional)) // Filtra por tipo ignorando mayúsculas/minúsculas
                .filter(c-> c.getFecha().equals(fecha))
                .filter(c-> !c.getEstado().equalsIgnoreCase("cancelada")) // Filtra solo los que no están cancelados
                .toList(); //recuerden siempre vovler a transformar el objeto en una nueva lista
    }

}