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

    private static final List<ModelAgenda> agendaList = new ArrayList<>();
    private int nextId = 1;

    public RepositoryAgenda() {
        agendaList.add(ModelAgenda.builder()
                .id(nextId++)
                .fecha(LocalDate.parse("2026-02-14"))
                .hora(LocalTime.parse("10:00"))
                .idProfesional("1")
                .idPaciente("20999255-8")
                .estado("reservada")
                .build());

        agendaList.add(ModelAgenda.builder()
                .id(nextId++)
                .fecha(LocalDate.parse("2026-02-15"))
                .hora(LocalTime.parse("11:00"))
                .idProfesional("2")
                .idPaciente("20999256-8")
                .estado("cancelada")
                .build());
    }

  


    public List<ModelAgenda> findAll() {
        return agendaList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModelAgenda::getId)) // Ordena por id ascendente
                .toList(); // Convierte el stream nuevamente a lista
    }


    public ModelAgenda save(ModelAgenda nuevaCita) {
        nuevaCita.setId(nextId++); 
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
                .filter(c-> c != null) // Filtra solo los que no son nulos para evitar NullPointerException
                .filter(c -> idProfesional != null && idProfesional.equals(c.getIdProfesional())) // Filtra por idProfesional, asegurando que no sea nulo
                .filter(c-> fecha != null && fecha.equals(c.getFecha())) // Filtra por fecha, asegurando que no sea nula
                .filter(c-> c.getEstado() != null && !c.getEstado().equalsIgnoreCase("cancelada")) // Filtra solo los que no están cancelados
                .toList(); //recuerden siempre vovler a transformar el objeto en una nueva lista
    }

}