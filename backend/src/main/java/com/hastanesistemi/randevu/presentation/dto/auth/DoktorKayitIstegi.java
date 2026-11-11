package com.hastanesistemi.randevu.presentation.dto.auth;

import com.hastanesistemi.randevu.domain.model.person.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bir yoneticinin yeni bir doktor hesabi acmasi icin kullandigi istek
 * govdesi. Gereksinim: "Doktor ... sisteme kayit edilebilecek." Bu
 * endpoint, {@code ROLE_YONETICI} yetkisi gerektirir (bkz.
 * {@code AuthController.doktorKayitOl}).
 */
public record DoktorKayitIstegi(
        @NotBlank String ad,
        @NotBlank String soyad,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "TC Kimlik No 11 haneli rakamlardan olusmalidir.") String tcKimlikNo,
        @NotBlank @Email String email,
        String telefonNo,
        LocalDate dogumTarihi,

        @NotBlank(message = "Sifre bos olamaz.")
        @Size(min = 6, message = "Sifre en az 6 karakter olmalidir.")
        String sifre,

        @NotBlank(message = "Calisan numarasi bos olamaz.")
        String calisanNo,

        LocalDate iseBaslamaTarihi,

        @NotNull(message = "Temel maas bos olamaz.")
        @PositiveOrZero(message = "Temel maas negatif olamaz.")
        BigDecimal temelMaas,

        @NotNull(message = "Uzmanlik alani secilmelidir.")
        Specialty uzmanlikAlani,

        String diplomaNo) {
}
