package com.hastanesistemi.randevu.infrastructure.persistence.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * {@code WorkingHours} deger nesnesinin JPA karsiligi. Bkz.
 * {@link AddressEmbeddable} ic dokumantasyonu — ayni gerekce (domain'in
 * JPA'dan bagimsiz kalmasi) burada da gecerlidir. {@code @ElementCollection}
 * ile bir koleksiyon olarak (ornegin bir doktorun uygunluk zamanlari listesi)
 * kullanilmak uzere tasarlanmistir.
 */
@Embeddable
public class WorkingHoursEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "gun")
    private DayOfWeek gun;

    @Column(name = "baslangic_saati")
    private LocalTime baslangicSaati;

    @Column(name = "bitis_saati")
    private LocalTime bitisSaati;

    protected WorkingHoursEmbeddable() {
        // JPA icin zorunlu bos constructor.
    }

    public WorkingHoursEmbeddable(DayOfWeek gun, LocalTime baslangicSaati, LocalTime bitisSaati) {
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
}
