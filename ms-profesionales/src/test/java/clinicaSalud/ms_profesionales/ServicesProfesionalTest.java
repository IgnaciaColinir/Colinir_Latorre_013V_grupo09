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
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiología")
                .build();

        requestDTO = new ProfesionalRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setEspecialidad("Cardiología");
    }

    @Test
    void guardar_Exitoso() {
        // GIVEN
        when(repository.save(any(ModeloProfesional.class))).thenReturn(profesional);

        // WHEN
        ProfesionalResponseDTO resultado = service.guardar(requestDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(repository, times(1)).save(any(ModeloProfesional.class));
    }

    @Test
    void obtenerPorRut_Encontrado() {
        // GIVEN
        when(repository.findById("12345678-9")).thenReturn(Optional.of(profesional));

        // WHEN
        ProfesionalResponseDTO resultado = service.obtenerPorRut("12345678-9");

        // THEN
        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getEspecialidad());
    }

    @Test
    void obtenerPorRut_NoEncontrado_LanzaExcepcion() {
        // GIVEN
        when(repository.findById("111-1")).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorRut("111-1");
        });
        
        assertTrue(exception.getMessage().contains("no encontrado"));
    }
}