package clinicaSalud.ms_servicios;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import clinicaSalud.ms_servicios.DTO.ServicioDTO;
import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;
import clinicaSalud.ms_servicios.Service.ServicioService;

@ExtendWith(MockitoExtension.class)
public class ServicioServiceTest {

    @Mock
    private ServicioRepository repository;

    @InjectMocks
    private ServicioService service;

    private Servicio modelo;
    private ServicioDTO dto;

    @BeforeEach
    void setUp() {
        modelo = new Servicio();
        modelo.setIdServicio(1L);
        modelo.setNombre("Ecotomografía");
        modelo.setDescripcion("Examen de imagen");
        modelo.setPrecio(20000);
        modelo.setRequiereAyuno(true);

        dto = new ServicioDTO();
        dto.setIdServicio(1L);
        dto.setNombre("Ecotomografía");
        dto.setDescripcion("Examen de imagen");
        dto.setPrecio(20000);
        dto.setRequiereAyuno(true);
    }

    @Test
    void guardar_Exitoso() {
        when(repository.save(any(Servicio.class))).thenReturn(modelo);

        ServicioDTO resultado = service.guardar(dto);

        assertNotNull(resultado);
        assertEquals("Ecotomografía", resultado.getNombre());
        verify(repository, times(1)).save(any(Servicio.class));
    }

    @Test
    void guardar_PrecioNegativo_LanzaExcepcion() {
        dto.setPrecio(-5000);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.guardar(dto));
        assertEquals("Error: El precio no puede ser negativo", ex.getMessage());
        verify(repository, never()).save(any(Servicio.class));
    }

    @Test
    void obtenerPorId_Exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(modelo));

        ServicioDTO resultado = service.obtenerPorId(1L);

        assertEquals(20000, resultado.getPrecio());
    }

    @Test
    void obtenerPorId_NoEncontrado_LanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.obtenerPorId(99L));
        assertEquals("Atención: El servicio con ID 99 no existe en nuestros registros.", ex.getMessage());
    }
}