package com.hastanesistemi.randevu.presentation.dto.ortak;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Tum hata yanitlarinda kullanilan ORTAK govde (bkz. {@code
 * GlobalHataYakalayici}). {@code alanHatalari}, bean validation (istek
 * govdesi dogrulama) hatalarinda alan-bazli mesajlari tasir; diger
 * hatalarda bos liste olur.
 */
public record HataYaniti(LocalDateTime zaman, int durumKodu, String mesaj, List<String> alanHatalari) {

    public static HataYaniti olustur(int durumKodu, String mesaj) {
        return new HataYaniti(LocalDateTime.now(), durumKodu, mesaj, List.of());
    }

    public static HataYaniti alanHatalariIle(int durumKodu, String mesaj, List<String> alanHatalari) {
        return new HataYaniti(LocalDateTime.now(), durumKodu, mesaj, alanHatalari);
    }
}
