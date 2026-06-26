import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateAwayRutesConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder,
            @Value("${AUTH_SERVICE_URL:lb://ms-auth}") String authServiceUrl,
            @Value("${USUARIOS_SERVICE_URL:lb://ms-usuarios}") String usuariosServiceUrl,
            @Value("${PACIENTE_SERVICE_URL:lb://ms-paciente}") String pacienteServiceUrl,
            @Value("${PROFESIONALES_SERVICE_URL:lb://ms-profesionales}") String profesionalesServiceUrl,
            @Value("${AGENDA_SERVICE_URL:lb://agenda}") String agendaServiceUrl,
            @Value("${CONSULTAS_SERVICE_URL:lb://ms-consultas}") String consultasServiceUrl,
            @Value("${FICHAS_SERVICE_URL:lb://ms-fichas}") String fichasServiceUrl,
            @Value("${SERVICIOS_SERVICE_URL:lb://ms-servicios}") String serviciosServiceUrl,
            @Value("${INVENTARIO_SERVICE_URL:lb://ms-inventario}") String inventarioServiceUrl,
            @Value("${PAGOS_SERVICE_URL:lb://ms-pagos}") String pagosServiceUrl) {

        return builder.routes()
                // 1. MS-AUTH
                .route("ms-auth", route -> route
                        .path("/api/v1/auth", "/api/v1/auth/**")
                        .uri(authServiceUrl))
                .route("ms-auth-openapi", route -> route
                        .path("/api-docs/ms-auth")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-auth", "/v3/api-docs"))
                        .uri(authServiceUrl))

                // 2. MS-USUARIOS
                .route("ms-usuarios", route -> route
                        .path("/api/v1/usuarios", "/api/v1/usuarios/**")
                        .uri(usuariosServiceUrl))
                .route("ms-usuarios-openapi", route -> route
                        .path("/api-docs/ms-usuarios")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-usuarios", "/v3/api-docs"))
                        .uri(usuariosServiceUrl))

                // 3. MS-PACIENTE
                .route("ms-paciente", route -> route
                        .path("/api/v1/pacientes", "/api/v1/pacientes/**")
                        .uri(pacienteServiceUrl))
                .route("ms-paciente-openapi", route -> route
                        .path("/api-docs/ms-paciente")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-paciente", "/v3/api-docs"))
                        .uri(pacienteServiceUrl))

                // 4. MS-PROFESIONALES
                .route("ms-profesionales", route -> route
                        .path("/api/v1/profesionales", "/api/v1/profesionales/**")
                        .uri(profesionalesServiceUrl))
                .route("ms-profesionales-openapi", route -> route
                        .path("/api-docs/ms-profesionales")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-profesionales", "/v3/api-docs"))
                        .uri(profesionalesServiceUrl))

                // 5. AGENDA
                .route("agenda-service", route -> route
                        .path("/api/v1/agenda", "/api/v1/agenda/**")
                        .uri(agendaServiceUrl))
                .route("agenda-service-openapi", route -> route
                        .path("/api-docs/agenda")
                        .filters(filters -> filters.rewritePath("/api-docs/agenda", "/v3/api-docs"))
                        .uri(agendaServiceUrl))

                // 6. MS-CONSULTAS
                .route("ms-consultas", route -> route
                        .path("/api/v1/consultas", "/api/v1/consultas/**")
                        .uri(consultasServiceUrl))
                .route("ms-consultas-openapi", route -> route
                        .path("/api-docs/ms-consultas")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-consultas", "/v3/api-docs"))
                        .uri(consultasServiceUrl))

                // 7. MS-FICHAS
                .route("ms-fichas", route -> route
                        .path("/api/v1/fichas", "/api/v1/fichas/**")
                        .uri(fichasServiceUrl))
                .route("ms-fichas-openapi", route -> route
                        .path("/api-docs/ms-fichas")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-fichas", "/v3/api-docs"))
                        .uri(fichasServiceUrl))

                // 8. MS-SERVICIOS
                .route("ms-servicios", route -> route
                        .path("/api/v1/servicios", "/api/v1/servicios/**")
                        .uri(serviciosServiceUrl))
                .route("ms-servicios-openapi", route -> route
                        .path("/api-docs/ms-servicios")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-servicios", "/v3/api-docs"))
                        .uri(serviciosServiceUrl))

                // 9. MS-INVENTARIO
                .route("ms-inventario", route -> route
                        .path("/api/v1/inventario", "/api/v1/inventario/**")
                        .uri(inventarioServiceUrl))
                .route("ms-inventario-openapi", route -> route
                        .path("/api-docs/ms-inventario")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-inventario", "/v3/api-docs"))
                        .uri(inventarioServiceUrl))

                // 10. MS-PAGOS
                .route("ms-pagos", route -> route
                        .path("/api/v1/pagos", "/api/v1/pagos/**")
                        .uri(pagosServiceUrl))
                .route("ms-pagos-openapi", route -> route
                        .path("/api-docs/ms-pagos")
                        .filters(filters -> filters.rewritePath("/api-docs/ms-pagos", "/v3/api-docs"))
                        .uri(pagosServiceUrl))

                .build();
    }
}


