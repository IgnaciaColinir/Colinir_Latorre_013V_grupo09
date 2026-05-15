package clinicaSalud.ms_servicios.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desactivamos protección antigua
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones, solo tokens
            .authorizeHttpRequests(auth -> auth
                // Aquí le decimos que TODAS las peticiones necesitan permiso
                .anyRequest().authenticated() 
            )
            // Ponemos a nuestro guardia antes del guardia por defecto de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); 

        return http.build();
    }
}