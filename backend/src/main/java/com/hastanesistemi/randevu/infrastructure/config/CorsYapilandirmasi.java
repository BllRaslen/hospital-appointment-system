package com.hastanesistemi.randevu.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Next.js frontend'in (varsayilan olarak http://localhost:3000'de calisan,
 * backend'den (http://localhost:8080) FARKLI bir origin'den gelen) API'ye
 * istek atabilmesi icin CORS (Cross-Origin Resource Sharing) izinlerini
 * tanimlar. Izin verilen origin, {@code CORS_ALLOWED_ORIGIN} ortam
 * degiskeniyle yapilandirilabilir - production'da gercek frontend domaini
 * verilecektir.
 */
@Configuration
public class CorsYapilandirmasi {

    @Value("${cors.allowed-origin:http://localhost:3000}")
    private String izinVerilenOrigin;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration yapilandirma = new CorsConfiguration();
        yapilandirma.setAllowedOrigins(List.of(izinVerilenOrigin));
        yapilandirma.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        yapilandirma.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        yapilandirma.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource kaynak = new UrlBasedCorsConfigurationSource();
        kaynak.registerCorsConfiguration("/**", yapilandirma);
        return kaynak;
    }
}
