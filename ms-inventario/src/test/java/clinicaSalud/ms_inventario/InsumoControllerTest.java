package clinicaSalud.ms_inventario;

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

import clinicaSalud.ms_inventario.Controller.InsumoController;
import clinicaSalud.ms_inventario.DTO.InsumoDTO;
import clinicaSalud.ms_inventario.Security.JwtFilter;
import clinicaSalud.ms_inventario.Security.JwtUtil;
import clinicaSalud.ms_inventario.Service.InsumoService;

@WebMvcTest(InsumoController.class)
@AutoConfigureMockMvc(addFilters = false) // Apaga el JWT para la prueba unitaria
public class InsumoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsumoService insumoService;

    // Mockeamos la seguridad
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private InsumoDTO dto;

    @BeforeEach
    void setUp() {
        dto = new InsumoDTO();
        dto.setIdInsumo(1L);
        dto.setNombre("Gasa");
        dto.setStockActual(10);
    }

    @Test
    void crearInsumo_RetornaOk() throws Exception {
        when(insumoService.guardar(any(InsumoDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/insumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gasa"));
    }

    @Test
    void obtenerTodos_RetornaOk() throws Exception {
        when(insumoService.obtenerTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/insumos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gasa"));
    }
}