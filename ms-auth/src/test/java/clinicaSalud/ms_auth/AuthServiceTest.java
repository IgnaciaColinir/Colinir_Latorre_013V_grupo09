package clinicaSalud.ms_auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import clinicaSalud.ms_auth.Security.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private AuthRequest request;
    private Usuario usuarioDB;

    @BeforeEach
    void setUp() {
        request = new AuthRequest();
        request.setUsername("test");
        request.setPassword("1234");

        usuarioDB = new Usuario();
        usuarioDB.setUsername("test");
        usuarioDB.setPassword("1234");
    }

    @Test
    void login_Exitoso() {
        when(repository.findByUsername("test")).thenReturn(Optional.of(usuarioDB));
        when(jwtUtil.generateToken("test")).thenReturn("token123");

        String res = authService.login(request);

        assertEquals("token123", res);
        verify(repository, times(1)).findByUsername("test");
    }
}