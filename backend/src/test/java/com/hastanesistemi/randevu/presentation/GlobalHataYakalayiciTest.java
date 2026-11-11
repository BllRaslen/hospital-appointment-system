package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.domain.exception.AppointmentConflictException;
import com.hastanesistemi.randevu.domain.exception.InvalidAppointmentStateException;
import com.hastanesistemi.randevu.domain.exception.InvalidAvailabilityException;
import com.hastanesistemi.randevu.presentation.dto.ortak.HataYaniti;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@code GlobalHataYakalayici}'nin, her domain istisna turunu DOGRU HTTP
 * durum koduna cevirdigini dogrulayan birim testleri. Spring context
 * baslatmadan (saf Java ile) test edilebilir, cunku handler metotlari
 * sadece istisna nesnesini parametre olarak alir.
 */
class GlobalHataYakalayiciTest {

    private final GlobalHataYakalayici yakalayici = new GlobalHataYakalayici();

    @Test
    void randevuCakismasi_409DonerVeMesajiTasir() {
        ResponseEntity<HataYaniti> yanit = yakalayici.cakismaVeDurumHatalari(
                new AppointmentConflictException("cakisma var"));

        assertEquals(HttpStatus.CONFLICT, yanit.getStatusCode());
        assertEquals("cakisma var", yanit.getBody().mesaj());
    }

    @Test
    void gecersizRandevuDurumu_409Doner() {
        ResponseEntity<HataYaniti> yanit = yakalayici.cakismaVeDurumHatalari(
                new InvalidAppointmentStateException("gecersiz durum gecisi"));

        assertEquals(HttpStatus.CONFLICT, yanit.getStatusCode());
    }

    @Test
    void uygunlukDisiZaman_400Doner() {
        ResponseEntity<HataYaniti> yanit = yakalayici.uygunlukHatasi(
                new InvalidAvailabilityException("uygun degil"));

        assertEquals(HttpStatus.BAD_REQUEST, yanit.getStatusCode());
    }

    @Test
    void gecersizArguman_400DonerVeAlanHatalariBosListedir() {
        ResponseEntity<HataYaniti> yanit = yakalayici.gecersizArgumanHatasi(
                new IllegalArgumentException("gecersiz girdi"));

        assertEquals(HttpStatus.BAD_REQUEST, yanit.getStatusCode());
        assertEquals(0, yanit.getBody().alanHatalari().size());
    }

    @Test
    void kimlikDogrulamaHatasi_401DonerVeMesajKullaniciyaDetayVermez() {
        ResponseEntity<HataYaniti> yanit = yakalayici.kimlikDogrulamaHatasi();

        assertEquals(HttpStatus.UNAUTHORIZED, yanit.getStatusCode());
        assertEquals("E-posta veya sifre hatali.", yanit.getBody().mesaj());
    }
}
