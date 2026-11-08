package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * {@code Clinic} domain sinifinin JPA karsiligi. Bkz.
 * {@link HealthcareFacilityEntity} (SINGLE_TABLE kalitim stratejisi).
 */
@Entity
@DiscriminatorValue("KLINIK")
public class ClinicEntity extends HealthcareFacilityEntity {

    @Column(name = "muayene_odasi_sayisi")
    private int muayeneOdasiSayisi;

    protected ClinicEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public int getMuayeneOdasiSayisi() {
        return muayeneOdasiSayisi;
    }

    public void setMuayeneOdasiSayisi(int muayeneOdasiSayisi) {
        this.muayeneOdasiSayisi = muayeneOdasiSayisi;
    }
}
