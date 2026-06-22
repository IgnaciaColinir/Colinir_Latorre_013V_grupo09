package clinicaSalud.ms_profesionales;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;
import clinicaSalud.ms_profesionales.Repository.RepositoryProfesionales;
import clinicaSalud.ms_profesionales.Services.ServicesProfesional;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;

@ExtendWith(MockitoExtension.class)
public class ServicesProfesionalTest {

    @Mock
    private RepositoryProfesionales repository;

    @InjectMocks
    private ServicesProfesional service;

    private ModeloProfesional profesional;
    private ProfesionalRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        profesional = ModeloProfesional.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiología")
                .email("juan@gmail.com")
                .telefono("987654321")
                .build();

        requestDTO = new ProfesionalRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setEspecialidad("Cardiología");
        requestDTO.setEmail("juan@gmail.com");
        requestDTO.setTelefono("987654321");
    }

    @Test
    void guardar_Exitoso() {
        // GIVEN: El rut no existe
        when(repository.existsByRut("12345678-9")).thenReturn(false);
        when(repository.save(any(ModeloProfesional.class))).thenReturn(profesional);

        // WHEN
        ProfesionalResponseDTO resultado = service.guardar(requestDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(repository, times(1)).save(any(ModeloProfesional.class));
    }

    @Test
    void guardar_Duplicado_LanzaExcepcion() {
        // GIVEN: El rut ya existe (rompe regla de negocio)
        when(repository.existsByRut("12345678-9")).thenReturn(true);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(requestDTO);
        });
        
        assertTrue(exception.getMessage().contains("Ya existe un profesional"));
        verify(repository, never()).save(any(ModeloProfesional.class));
    }

    @Test
    void obtenerPorRut_Encontrado() {
        // GIVEN
        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(profesional));

        // WHEN
        ProfesionalResponseDTO resultado = service.obtenerPorRut("12345678-9");

        // THEN
        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getEspecialidad());
    }

    @Test
    void obtenerPorRut_NoEncontrado_LanzaExcepcion() {
        // GIVEN
        when(repository.findByRut("111-1")).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.obtenerPorRut("111-1");
        });
        
        assertTrue(exception.getMessage().contains("no encontrado"));
    }
}