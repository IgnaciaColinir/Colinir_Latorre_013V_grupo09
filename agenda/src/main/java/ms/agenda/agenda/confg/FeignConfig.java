package ms.agenda.agenda.confg;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

     @Bean
    public Request.Options requestOptions() {
        // 3 segundos para conectarse y 5 para leer la respuesta
        return new Request.Options(3, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // Extrae el encabezado Authorization (Bearer eyJhbG...) que enviaste en Postman
                String authorizationHeader = request.getHeader("Authorization");
                
                if (authorizationHeader != null) {
                    // Se lo inyecta a la llamada saliente de Feign hacia Pacientes
                    requestTemplate.header("Authorization", authorizationHeader);
                }
            }
        };
    }


}
