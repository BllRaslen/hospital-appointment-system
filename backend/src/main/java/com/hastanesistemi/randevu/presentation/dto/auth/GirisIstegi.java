package com.hastanesistemi.randevu.presentation.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Giris (login) istegi govdesi. Kullanici, e-posta ve sifresiyle kimligini
 * kanitlar; basarili olursa {@code AuthController} bir JWT doner.
 */
public record GirisIstegi(
        @NotBlank(message = "E-posta bos olamaz.")
        @Email(message = "Gecerli bir e-posta adresi girin.")
        String email,

        @NotBlank(message = "Sifre bos olamaz.")
        String sifre) {
}
