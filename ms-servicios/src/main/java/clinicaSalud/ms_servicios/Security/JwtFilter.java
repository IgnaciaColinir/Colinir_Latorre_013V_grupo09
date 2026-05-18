package clinicaSalud.ms_servicios.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Buscamos 
        String authHeader = request.getHeader("Authorization");

        // 2. Si trae el token y empieza con "Bearer ", lo revisamos
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 3. Si el token es válido, qe pase
            if (jwtUtil.validarToken(token)) {
                String username = jwtUtil.extraerUsuario(token);
                
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        // 4. Que siga su camino (si no traía token válido, Spring Security lo va a rebotar)
        filterChain.doFilter(request, response);
    }
}