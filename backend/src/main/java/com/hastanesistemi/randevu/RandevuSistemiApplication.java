package com.hastanesistemi.randevu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Uygulamanin giris noktasi (entry point).
 * <p>
 * Not: Bu sinif "frameworks & drivers" katmanina aittir (Clean Architecture'in
 * en dis halkasi). Domain katmani (com.hastanesistemi.randevu.domain) bu
 * sinifa veya Spring'e HICBIR BAGIMLILIK icermez - bagimlilik oku iceriye
 * dogrudur (Dependency Rule).
 */
@SpringBootApplication
public class RandevuSistemiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandevuSistemiApplication.class, args);
    }
}
