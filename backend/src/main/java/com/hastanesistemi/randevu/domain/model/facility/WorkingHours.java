package com.hastanesistemi.randevu.domain.model.facility;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Bir haftanin belirli bir gunundeki calisma saati araligini temsil eden
 * degismez (immutable) deger nesnesi. Hem tesisin (hastane/klinik) genel
 * calisma saatleri hem de bir doktorun kisisel uygunluk saatleri icin
 * kullanilir.
 * <p>
 * OOP ilkesi: KAPSULLEME - gun/baslangicSaati/bitisSaati sadece constructor'da
 * atanir, sonradan degistirilemez.
 */
public final class WorkingHours {

    private final DayOfWeek gun;
    private final LocalTime baslangicSaati;
    private final LocalTime bitisSaati;

    public WorkingHours(DayOfWeek gun, LocalTime baslangicSaati, LocalTime bitisSaati) {
        if (gun == null) {
            throw new IllegalArgumentException("Gun bilgisi bos olamaz.");
        }
        if (baslangicSaati == null || bitisSaati == null || !bitisSaati.isAfter(baslangicSaati)) {
            throw new IllegalArgumentException("Bitis saati, baslangic saatinden sonra olmalidir.");
        }
        this.gun = gun;
        this.baslangicSaati = baslangicSaati;
        this.bitisSaati = bitisSaati;
    }

    public DayOfWeek getGun() {
        return gun;
    }

    public LocalTime getBaslangicSaati() {
        return baslangicSaati;
    }

    public LocalTime getBitisSaati() {
        return bitisSaati;
    }

    @Override
    public String toString() {
        return gun + " " + baslangicSaati + "-" + bitisSaati;
    }
}
