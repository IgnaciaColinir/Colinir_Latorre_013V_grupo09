package ms.usuarios.ms.usuarios.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ms.usuarios.ms.usuarios.Model.ModeloUsuario;
import ms.usuarios.ms.usuarios.Repository.RepositoryUsuarios;
import ms.usuarios.ms.usuarios.Services.ServicesUsuario;
import ms.usuarios.ms.usuarios.dto.request.UsuarioRequestDTO;
import ms.usuarios.ms.usuarios.dto.response.UsuarioResponseDTO;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServicesTest {

    @Autowired
    private ServicesUsuario servicesUsuario;

    // Crea un MOCK del repositorio de usuarios. No toca MySQL; se define con when(...).
    @MockitoBean
    private RepositoryUsuarios usuariosRepository;

    @Test
    public void testObtenerTodos() {
        // PASO 1 (Mock): Datos simulados en la base de datos
        ModeloUsuario usuarioMock = ModeloUsuario.builder()
                .rut("12345678-9")
                .nombre("Hanses")
                .apellido("Montilva")
                .email("hanses@clinica.cl")
                .password("secret hash")
                .cargo("ADMINISTRADOR")
                .build();

        when(usuariosRepository.findAll()).thenReturn(List.of(usuarioMock));

        // PASO 2 (Ejecutar): Llama al método real de tu servicio
        List<UsuarioResponseDTO> resultado = servicesUsuario.obtenerTodos();

        // PASO 3 (Verificar): Comprobar que el mapeo a ResponseDTO se hace con éxito
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("12345678-9", resultado.get(0).getRut());
        assertEquals("Hanses", resultado.get(0).getNombre());
        assertEquals("ADMINISTRADOR", resultado.get(0).getCargo());
    }

    @Test
    public void testObtenerPorRut() {
        String rutTest = "12345678-9";
        ModeloUsuario usuarioMock = ModeloUsuario.builder()
                .rut(rutTest)
                .nombre("Hanses")
                .apellido("Montilva")
                .email("hanses@clinica.cl")
                .cargo("MEDICO")
                .build();

        // Configuramos el findById de JPA para que retorne el Optional correspondiente
        when(usuariosRepository.findById(rutTest)).thenReturn(Optional.of(usuarioMock));

        // PASO 2 (Ejecutar)
        UsuarioResponseDTO found = servicesUsuario.obtenerPorRut(rutTest);

        // PASO 3 (Verificar)
        assertNotNull(found);
        assertEquals(rutTest, found.getRut());
        assertEquals("MEDICO", found.getCargo());
    }

    @Test
    public void testGuardar() {
        // Objeto Request que envía el controlador/usuario
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Hanses");
        request.setApellido("Montilva");
        request.setEmail("hanses@clinica.cl");
        request.setPassword("12345");
        request.setCargo("MEDICO");

        ModeloUsuario usuarioMockGuardado = ModeloUsuario.builder()
                .rut("12345678-9")
                .nombre("Hanses")
                .apellido("Montilva")
                .email("hanses@clinica.cl")
                .password("12345")
                .cargo("MEDICO")
                .build();

        // Simulamos la validaciones internas (deben devolver Optional.empty() para que proceda)
        when(usuariosRepository.findById(request.getRut())).thenReturn(Optional.empty());
        when(usuariosRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // Simulamos el guardado
        when(usuariosRepository.save(any(ModeloUsuario.class))).thenReturn(usuarioMockGuardado);

        // PASO 2 (Ejecutar)
        UsuarioResponseDTO saved = servicesUsuario.guardar(request);

        // PASO 3 (Verificar)
        assertNotNull(saved);
        assertEquals("12345678-9", saved.getRut());
        assertEquals("MEDICO", saved.getCargo());
    }

    @Test
    public void testEliminarExitoso() {
        String rutEliminar = "12345678-9";

        // Simulamos que el usuario sí existe en la BD
        when(usuariosRepository.existsById(rutEliminar)).thenReturn(true);
        doNothing().when(usuariosRepository).deleteById(rutEliminar);

        // PASO 2 (Ejecutar)
        boolean eliminado = servicesUsuario.eliminar(rutEliminar);

        // PASO 3 (Verificar): El método de tu lógica retorna true si borra
        assertTrue(eliminado);
        verify(usuariosRepository, times(1)).deleteById(rutEliminar);
    }

}
