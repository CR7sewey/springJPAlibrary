package com.mike.springjpalibrary.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library API",
                version = "v1",
                contact = @Contact(
                        name = "Miguel"
                ),
                summary = "This is a summary"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth") // requisito de segurança da api
        }
)
@SecurityScheme(
        name = "bearerAuth", // definicao da segurança
        type = SecuritySchemeType.HTTP, // pq envio o token via http
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER // adiciona no header
)
public class OpenAPIConfiguration {
}
