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

    public List<UsuarioResponseDTO> obtenerTodos() {
        try {
            List<ModeloUsuario> usuarios = usuariosRepository.findAll();
            return usuarios.stream().map(usuario -> {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setRut(usuario.getRut());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setEmail(usuario.getEmail());
            dto.setCargo(usuario.getCargo());
            
            // Como el cargo ahora es un objeto (ModeloCargo), extraemos su ID o nombre de texto
            /*if (usuario.getCargo() != null) {
                dto.setCargo(usuario.getCargo().getIdCargo()); 
            }*/
            
            return dto;
        }).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage());
        }
    }
    

    public UsuarioResponseDTO obtenerPorRut(String rut) {
        try {
            ModeloUsuario usuario = usuariosRepository.findById(rut).orElse(null);
            if (usuario == null) {
                throw new RuntimeException("Usuario con rut " + rut + " no encontrado");
            }
            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setRut(usuario.getRut());
            response.setNombre(usuario.getNombre());
            response.setApellido(usuario.getApellido());
            response.setEmail(usuario.getEmail());
            response.setCargo(usuario.getCargo());
            return response;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage());
        }
    }
    
   

    
    public UsuarioResponseDTO guardar(UsuarioRequestDTO request) {
    try {

        // 1. Verificar si el usuario ya existe
        ModeloUsuario existente = usuariosRepository.findById(request.getRut()).orElse(null);
        
        /*REVISAR CON RODRIGO */
        /*// Creas el objeto Cargo con el ID que viene del DTO
            ModeloCargo cargoAsignado = new ModeloCargo();
            cargoAsignado.setIdCargo(request.getCargo().toUpperCase());

            // Se lo pasas a tu entidad
            usuarioParaGuardar.setCargo(cargoAsignado); */
        if (existente != null) {
            throw new RuntimeException("El usuario con rut " + request.getRut() + " ya existe");
        }

        // 2. Verificar que el correo no esté duplicado
        ModeloUsuario emailExistente = usuariosRepository.findByEmail(request.getEmail()).orElse(null);

        if (emailExistente != null) {
            throw new RuntimeException("El email " + request.getEmail() + " ya esta asociado a un usuario");
        }

        // 3. Restringir los Usuarios permitidos
        String cargo = request.getCargo().toUpperCase();
        if (!cargo.equals("ADMINISTRADOR") && !cargo.equals("MEDICO") && 
            !cargo.equals("ADMINISTRATIVO") && !cargo.equals("CONTABLE")) {
            throw new RuntimeException("Cargo no válido. Debe ser ADMINISTRADOR, MEDICO, ADMINISTRATIVO o CONTABLE");
        }

        
        // 4. MAPEO: Pasar los datos del Request DTO a la Entidad ModeloUsuario para la BD
        ModeloUsuario usuarioParaGuardar = new ModeloUsuario();
        usuarioParaGuardar.setRut(request.getRut());
        usuarioParaGuardar.setNombre(request.getNombre());
        usuarioParaGuardar.setApellido(request.getApellido());
        usuarioParaGuardar.setEmail(request.getEmail());
        usuarioParaGuardar.setPassword(request.getPassword()); // Nota: Idealmente aquí deberías encriptarla en el futuro
        usuarioParaGuardar.setCargo(cargo); // Guardamos en mayúsculas para estandarizar

        // 5. Guardar la entidad en la Base de Datos
        ModeloUsuario usuarioGuardado = usuariosRepository.save(usuarioParaGuardar);

        // 6. MAPEO DE SALIDA: Convertir la entidad guardada al DTO de respuesta que el Controller espera
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setRut(usuarioGuardado.getRut());
        response.setNombre(usuarioGuardado.getNombre());
        response.setApellido(usuarioGuardado.getApellido());
        response.setEmail(usuarioGuardado.getEmail());
        response.setCargo(usuarioGuardado.getCargo());
        // 💡 Ojo: Aquí NO le seteamos el password al response para proteger la seguridad del usuario

        return response;

    } catch (RuntimeException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error fatal e inesperado al registrar el usuario: " + e.getMessage());
    }
    }
    
    public UsuarioResponseDTO actualizar(String rut, UsuarioRequestDTO request) {
        try {
            ModeloUsuario usuarioExistente = usuariosRepository.findById(rut).orElseThrow(()-> new RuntimeException("Usuario con rut " + rut + " no encontrado"));

            if(!usuarioExistente.getEmail().equals(request.getEmail())){
                ModeloUsuario emailDuplicado = usuariosRepository.findByEmail(request.getEmail()).orElse(null);
                if(emailDuplicado != null){
                    throw new RuntimeException("El email " + request.getEmail() + " ya esta asociado a otro usuario");
                }
                usuarioExistente.setEmail(request.getEmail());
            }

            String cargo = request.getCargo().toUpperCase();
            if (!cargo.equals("ADMINISTRADOR") && !cargo.equals("MEDICO") && 
                !cargo.equals("ADMINISTRATIVO") && !cargo.equals("CONTABLE")) {
                throw new RuntimeException("Cargo no válido. Debe ser ADMINISTRADOR, MEDICO, ADMINISTRATIVO o CONTABLE");
            }

            usuarioExistente.setNombre(request.getNombre());
            usuarioExistente.setApellido(request.getApellido());
            usuarioExistente.setCargo(cargo);

            ModeloUsuario usuarioActualizado = usuariosRepository.save(usuarioExistente);

            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setRut(usuarioActualizado.getRut());
            response.setNombre(usuarioActualizado.getNombre());
            response.setApellido(usuarioActualizado.getApellido());
            response.setEmail(usuarioActualizado.getEmail());
            response.setCargo(usuarioActualizado.getCargo());
            
            return response;
        } catch (RuntimeException e) { 
            throw e;    
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }
   
    public boolean eliminar(String rut) {
        try {
            if (usuariosRepository.existsById(rut)){
                usuariosRepository.deleteById(rut);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
    }
    

    public UsuarioResponseDTO obtenerPorEmail(String email) {
        try {
            ModeloUsuario usuario = usuariosRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario con email " + email + " no encontrado"));

            // 2. MAPEO: Convertimos la entidad de la BD al DTO de salida (sin contraseña)
            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setRut(usuario.getRut());
            response.setNombre(usuario.getNombre());
            response.setApellido(usuario.getApellido());
            response.setEmail(usuario.getEmail());
            response.setCargo(usuario.getCargo());

            return response;

        } catch (RuntimeException e) {
            // Volvemos a lanzar el "no encontrado" para que lo capture el controlador
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al buscar el usuario por email: " + e.getMessage());
        }
    }


    public List<UsuarioResponseDTO> obtenerPorCargo(String cargo) {
        try {
            List<ModeloUsuario> usuarios = usuariosRepository.findByCargo(cargo);

            if (usuarios.isEmpty()) {
                throw new RuntimeException("No se encontraron usuarios con el cargo " + cargo);
            }

            return usuarios.stream().map(usuario -> {
                UsuarioResponseDTO response = new UsuarioResponseDTO();
                response.setRut(usuario.getRut());
                response.setNombre(usuario.getNombre());
                response.setApellido(usuario.getApellido());
                response.setEmail(usuario.getEmail());
                response.setCargo(usuario.getCargo());
                return response;
            }).toList();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al buscar los usuarios por cargo: " + e.getMessage());
        }
    }
    
}

       


