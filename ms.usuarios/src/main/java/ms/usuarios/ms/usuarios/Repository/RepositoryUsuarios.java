package ms.usuarios.ms.usuarios.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Repository;

import ms.usuarios.ms.usuarios.Model.ModeloUsuario;

@Repository
public class RepositoryUsuarios {
    private final List<ModeloUsuario> usuariosList = new ArrayList<>();
  

    public RepositoryUsuarios() {
        usuariosList.add(ModeloUsuario.builder()
             
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .password("password123")
                .cargo("Administrador")
                .build());

        usuariosList.add(ModeloUsuario.builder()
                
                .rut("98765432-1")
                .nombre("Maria")
                .apellido("Gonzalez")
                .email("maria.gonzalez@example.com")
                .password("password456")
                .cargo("Enfermera")
                .build());
    }





    public List<ModeloUsuario> findAll() {
        return usuariosList.stream() // Convierte la lista en stream para poder aplicar operaciones
                .sorted(Comparator.comparing(ModeloUsuario:: getRut)) // Ordena por id ascendente
                .toList(); // Convierte el stream nuevamente a lista

    
    }

    public List <ModeloUsuario> findByRut(String rut) {
        return usuariosList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getRut().equals(rut)) // Filtra dejando solo el que coincide con el id
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }

    public List <ModeloUsuario> findByEmail(String email) {
        return usuariosList.stream() // Stream para recorrer la colección 
                .filter(p -> p.getEmail().equals(email)) // Filtra dejando solo el que coincide con el email
                .toList();// vuelve a empaquetar todo en una Lista IMPOTANTE AGREGARLO!!!!!
    }



    public ModeloUsuario save(ModeloUsuario nuevoUsuario) {
        usuariosList.add(nuevoUsuario);
        return nuevoUsuario;
    }

    public boolean deleteByRut(String rut) {
        return usuariosList.removeIf(p -> p.getRut().equals(rut));
    }
    
    public List<ModeloUsuario> findbyCargo(String cargo) {
        return usuariosList.stream() // convierte la lista a un formato legible por java asincronamente
                .filter(p -> p.getCargo().equalsIgnoreCase(cargo)) // Filtra por cargo ignorando mayúsculas/minúsculas
                .toList(); 
    }

    public ModeloUsuario update(String rut, ModeloUsuario usuarioActualizado) {

        for (int i = 0; i < usuariosList.size(); i++) {

            if (usuariosList.get(i).getRut().equalsIgnoreCase(rut)){

                
                usuariosList.get(i).setNombre(usuarioActualizado.getNombre());
                usuariosList.get(i).setApellido(usuarioActualizado.getApellido());
                usuariosList.get(i).setEmail(usuarioActualizado.getEmail());
                usuariosList.get(i).setPassword(usuarioActualizado.getPassword());
                usuariosList.get(i).setCargo(usuarioActualizado.getCargo());

                return usuariosList.get(i);
            }
        }    

        return null;

    }



}
