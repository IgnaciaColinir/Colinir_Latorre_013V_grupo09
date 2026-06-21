package clinicaSalud.ms_servicios;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import clinicaSalud.ms_servicios.Controller.ServicioController;
import clinicaSalud.ms_servicios.DTO.ServicioDTO;
import clinicaSalud.ms_servicios.Security.JwtFilter;
import clinicaSalud.ms_servicios.Security.JwtUtil;
import clinicaSalud.ms_servicios.Service.ServicioService;

@WebMvcTest(ServicioController.class)
@AutoConfigureMockMvc(addFilters = false) // JWT apagado
public class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioService servicioService;

    @MockBean
    private JwtFilter jwtFilter;
    
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private ServicioDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ServicioDTO();
        dto.setIdServicio(1L);
        dto.setNombre("Ecotomografía");
        dto.setDescripcion("Examen de imagen");
        dto.setPrecio(20000);
        dto.setRequiereAyuno(true);
    }

    @Test
    void crear_RetornaCreated() throws Exception {
        when(servicioService.guardar(any(ServicioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/servicios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.nombre").value("Ecotomografía"));
    }

    @Test
    void listar_RetornaOk() throws Exception {
        when(servicioService.listarTodo()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precio").value(20000));
    }
}