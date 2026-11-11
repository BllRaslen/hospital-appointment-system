package com.hastanesistemi.randevu.presentation.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Hastanin kendi kendine kayit (self sign-up) istegi govdesi. Gereksinim:
 * "Hasta ... sisteme kayit edilebilecek." TC kimlik no/ad/soyad/e-posta
 * formati icin burada uygulanan kurallar, {@code Person} domain sinifindaki
 * constructor dogrulamasiyla AYNIDIR - gecersiz bir deger, HTTP katmaninda
 * (400) domain katmanina ulasmadan ONCE yakalanir; domain constructor'i
 * ikinci bir savunma hatti olarak kalir.
 */
public record HastaKayitIstegi(
        @NotBlank(message = "Ad bos olamaz.")
        String ad,

        @NotBlank(message = "Soyad bos olamaz.")
        String soyad,

        @NotBlank(message = "TC Kimlik No bos olamaz.")
        @Pattern(regexp = "\\d{11}", message = "TC Kimlik No 11 haneli rakamlardan olusmalidir.")
        String tcKimlikNo,

        @NotBlank(message = "E-posta bos olamaz.")
        @Email(message = "Gecerli bir e-posta adresi girin.")
        String email,

        String telefonNo,

        LocalDate dogumTarihi,

        @NotBlank(message = "Sifre bos olamaz.")
        @Size(min = 6, message = "Sifre en az 6 karakter olmalidir.")
        String sifre,

        String kanGrubu,

        String sigortaNo) {
}
