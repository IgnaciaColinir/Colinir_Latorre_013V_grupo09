package ms.paciente.ms.paciente.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import ms.paciente.ms.paciente.Model.ModeloPaciente;

@Repository
public class RepositoryPacientes {
    private final List<ModeloPaciente> pacienteList = new ArrayList<>();
     public RepositoryPacientes() {
        pacienteList.add(ModeloPaciente.builder()
                .rut("20999255-8")
                .nombre("Fabian")
                .apellido("Navarro")
                .direccion("3 poniente 1234")
                .fechaNacimiento(LocalDate.of(2002, 4, 5))
                .telefono("974567890")
                .email("fabian.navarro@example.com")
                .prevision("fonasa")
                .rutTutor("20999255-8")
                .nombreTutor("Fabian Navarro")
                .build());

        pacienteList.add(ModeloPaciente.builder()
                .rut("20999256-9")
                .nombre("Hans")
                .apellido("Pinilla")
                .direccion("4 poniente 5678")
                .fechaNacimiento(LocalDate.of(2000, 12, 12))
                .telefono("987654321")
                .email("hans.pinilla@example.com")
                .prevision("fonasa")
                .rutTutor("20999256-9")
                .nombreTutor("Hans Pinilla")
                .build());
    }
    

    public List<ModeloPaciente> findAll() {
        return pacienteList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModeloPaciente:: getRut)) // Ordena por id ascendente
                .collect(Collectors.toList()); // Convierte el stream nuevamente a lista

    
    }

    public List <ModeloPaciente> findByRut(String rut) {
        return pacienteList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getRut().equals(rut)) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }


    public List <ModeloPaciente> findByPrevision(String prevision) {
        return pacienteList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getPrevision().equals(prevision)) // Filtra dejando solo el que coincide con la previsión
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }


    public ModeloPaciente save(ModeloPaciente nuevoPaciente) {
        pacienteList.add(nuevoPaciente);
        return nuevoPaciente;
    }

    public boolean deleteByRut(String rut) {
        return pacienteList.removeIf(p -> p.getRut().trim().equalsIgnoreCase(rut.trim()));
    }
    
    public ModeloPaciente update(String rut, ModeloPaciente pacienteActualizado) {

        for (int i = 0; i < pacienteList.size(); i++) {

            if (pacienteList.get(i).getRut().equalsIgnoreCase(rut)){

                pacienteList.get(i).setNombre(pacienteActualizado.getNombre());
                pacienteList.get(i).setApellido(pacienteActualizado.getApellido());
                pacienteList.get(i).setDireccion(pacienteActualizado.getDireccion());
                pacienteList.get(i).setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
                pacienteList.get(i).setTelefono(pacienteActualizado.getTelefono());
                pacienteList.get(i).setEmail(pacienteActualizado.getEmail());
                pacienteList.get(i).setPrevision(pacienteActualizado.getPrevision());
                pacienteList.get(i).setRutTutor(pacienteActualizado.getRutTutor());
                pacienteList.get(i).setNombreTutor(pacienteActualizado.getNombreTutor());
                return pacienteList.get(i);
            }
        }    

        return null;

    }



}
