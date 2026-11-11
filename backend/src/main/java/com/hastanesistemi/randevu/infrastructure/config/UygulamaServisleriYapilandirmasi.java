package com.hastanesistemi.randevu.infrastructure.config;

import com.hastanesistemi.randevu.application.port.out.AppointmentRepository;
import com.hastanesistemi.randevu.application.service.RandevuServisi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application katmanindaki use-case servislerini Spring baglamina (context)
 * BAGLAYAN (wiring) yapilandirma sinifi.
 * <p>
 * {@code RandevuServisi} KASITLI OLARAK hicbir Spring anotasyonu ICERMEZ
 * (bkz. kendi ic dokumantasyonu) - application katmani, Clean Architecture'in
 * Bagimlilik Kuralina gore framework'ten TAMAMEN bagimsiz, saf Java olarak
 * kalmalidir. "Framework'e baglama" sorumlulugu, en dis katmanda
 * (infrastructure) burada, TEK bir yerde toplanir; application katmani kodu
 * hicbir zaman degismez, sadece bu yapilandirma sinifi degisir.
 */
@Configuration
public class UygulamaServisleriYapilandirmasi {

    @Bean
    public RandevuServisi randevuServisi(AppointmentRepository appointmentRepository) {
        return new RandevuServisi(appointmentRepository);
    }
}
