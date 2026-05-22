package ms.usuarios.ms.usuarios.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuarios.ms.usuarios.Model.ModeloUsuario;
import ms.usuarios.ms.usuarios.Repository.RepositoryUsuarios;
import ms.usuarios.ms.usuarios.dto.request.UsuarioRequestDTO;
import ms.usuarios.ms.usuarios.dto.response.UsuarioResponseDTO;

@Service
public class ServicesUsuario {

    @Autowired
    private RepositoryUsuarios usuariosRepository;

    public List<ModeloUsuario> obtenerTodos() {
        try {
            return usuariosRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage());
        }
    }
    

    public List <ModeloUsuario> obtenerPorRut(String rut) {
        try {
            return usuariosRepository.findByRut(rut);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage());
        }
    }
    
      public List <ModeloUsuario> obtenerPorEmail(String email) {
        try {
            return usuariosRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar email: " + e.getMessage());
        }
    }

    
    public UsuarioResponseDTO guardar(UsuarioRequestDTO request) {

        ModeloUsuario existente = usuariosRepository.findByRut(request.getRut()).stream().findFirst().orElse(null);

        if (existente != null) {
            throw new RuntimeException("El usuario con rut " + request.getRut() + " ya existe");
        }

        ModeloUsuario emailExistente = usuariosRepository.findByEmail(request.getEmail()).stream().findFirst().orElse(null);

        if (emailExistente != null) {
            throw new RuntimeException("El email " + request.getEmail() + " ya esta asociado a un usuario");
        }

        String cargo = request.getCargo().toUpperCase();
        if (!cargo.equals("ADMINISTRADOR") && !cargo.equals("MEDICO") && !cargo.equals("ADMINISTRATIVO") 
            && !cargo.equals("CONTABLE")){
            throw new RuntimeException("Cargo no válido. Debe ser ADMINISTRADOR, MEDICO, ADMINISTRATIVO o CONTABLE");
        }

        ModeloUsuario usuario = ModeloUsuario.builder()

                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email((request.getEmail()))
                .password((request.getPassword()))
                .cargo((request.getCargo()))
                .build();

        ModeloUsuario guardado = usuariosRepository.save(usuario);

        return UsuarioResponseDTO.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .email((guardado.getEmail()))
                .password((guardado.getPassword()))
                .cargo((guardado.getCargo()))
                .build();
    }
    
    public ModeloUsuario actualizar(String rut, ModeloUsuario usuarioActualizado) {
            try {
                return usuariosRepository.update(rut, usuarioActualizado);
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
            }
    }

    public boolean eliminar(String rut) {
            try {
                return usuariosRepository.deleteByRut(rut);
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
            }
    }

    public List<ModeloUsuario> buscarPorCargo(String cargo) {
        try {
            return usuariosRepository.findbyCargo(cargo);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por cargo: " + e.getMessage());
        }
    }       

}
