package clinicaSalud.api_gateway.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SwaggerUiController {

    private final Resource swaggerUiHtml = new ClassPathResource("static/swagger-ui.html");

    @GetMapping(value = {"/swagger-ui.html", "/swagger-ui/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    public Mono<ResponseEntity<Resource>> swaggerUi() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(swaggerUiHtml));
    }
}