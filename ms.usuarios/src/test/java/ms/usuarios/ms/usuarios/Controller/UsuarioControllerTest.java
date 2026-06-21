package ms.usuarios.ms.usuarios.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.usuarios.ms.usuarios.Controllers.UsuarioController;
import ms.usuarios.ms.usuarios.Services.ServicesUsuario;
import ms.usuarios.ms.usuarios.dto.request.UsuarioRequestDTO;
import ms.usuarios.ms.usuarios.dto.response.UsuarioResponseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;




@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockitoBean
    private ServicesUsuario usuarioServices; // Sincronizado con el nombre de tu atributo @Autowired

    @Autowired
    private ObjectMapper objectMapper; 

    private UsuarioResponseDTO usuarioResponseMock;
    private UsuarioRequestDTO usuarioRequestMock;

    @BeforeEach
    void setUp() {
        usuarioResponseMock = new UsuarioResponseDTO();
        usuarioResponseMock.setRut("12345678-9");
        usuarioResponseMock.setNombre("Hanses");
        usuarioResponseMock.setApellido("Montilva");
        usuarioResponseMock.setEmail("hanses@clinica.cl");
        usuarioResponseMock.setCargo("ADMINISTRADOR");

        usuarioRequestMock = new UsuarioRequestDTO();
        usuarioRequestMock.setRut("12345678-9");
        usuarioRequestMock.setNombre("Hanses");
        usuarioRequestMock.setApellido("Montilva");
        usuarioRequestMock.setEmail("hanses@clinica.cl");
        usuarioRequestMock.setPassword("password123");
        usuarioRequestMock.setCargo("ADMINISTRADOR");
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(usuarioServices.obtenerTodos()).thenReturn(List.of(usuarioResponseMock));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombre").value("Hanses"))
                .andExpect(jsonPath("$[0].cargo").value("ADMINISTRADOR"));
    }

    @Test
    public void testObtenerPorRut() throws Exception {
        String rutTest = "12345678-9";
        when(usuarioServices.obtenerPorRut(rutTest)).thenReturn(usuarioResponseMock);

        mockMvc.perform(get("/api/v1/usuarios/rut/" + rutTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Hanses"));
    }

    @Test
    public void testGuardar() throws Exception {
        when(usuarioServices.guardar(any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseMock);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestMock)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.email").value("hanses@clinica.cl"));
    }

    @Test
    public void testActualizar() throws Exception {
        String rutTest = "12345678-9";
        when(usuarioServices.actualizar(eq(rutTest), any(UsuarioRequestDTO.class))).thenReturn(usuarioResponseMock);

        // Path variable /{rut} directo
        mockMvc.perform(put("/api/v1/usuarios/" + rutTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testEliminarExitoso() throws Exception {
        String rutTest = "12345678-9";
        when(usuarioServices.eliminar(rutTest)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/" + rutTest))
                .andExpect(status().isOk()) 
                .andExpect(content().string("Eliminado con éxito")); 
    }

    @Test
    public void testEliminarNoEncontrado() throws Exception {
        String rutTest = "99999999-9";
        when(usuarioServices.eliminar(rutTest)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/usuarios/" + rutTest))
                .andExpect(status().isNotFound()) 
                .andExpect(content().string("Usuario con rut " + rutTest + " no encontrado"));
    }


}
