package ms.pagos.ms.pagos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

    String path = request.getRequestURI();
    
    // 1. Si es una ruta pública, dejamos pasar de inmediato y salimos del método
    if (path.startsWith("/auth/login") || path.startsWith("/api/v1/pagos")) {
        filterChain.doFilter(request, response);
        return;
    }

    String authHeader = request.getHeader("Authorization");

    // 2. VALIDACIÓN CRÍTICA: Si el header es nulo o no empieza con "Bearer ", 
    // no intentamos cortar el texto. Dejamos que Spring Security decida si rechaza o no.
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    // 3. Ahora es seguro hacer el substring porque ya validamos el "Bearer "
    String token = authHeader.substring(7);

    try {
        if (jwtService.tokenValido(token)) {
            String username = jwtService.obtenerUsername(token);

            //TODO: En el futuro, reemplaza List.of() por las autoridades reales de tu token
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception e) {
        // Si el token está mal formateado o expiró, limpiamos el contexto de seguridad
        SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
}
}