package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * {@code Hospital} domain sinifinin JPA karsiligi. Bkz.
 * {@link HealthcareFacilityEntity} (SINGLE_TABLE kalitim stratejisi).
 */
@Entity
@DiscriminatorValue("HASTANE")
public class HospitalEntity extends HealthcareFacilityEntity {

    @Column(name = "yatak_sayisi")
    private int yatakSayisi;

    @Column(name = "acil_servis_var_mi")
    private boolean acilServisVarMi;

    protected HospitalEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public int getYatakSayisi() {
        return yatakSayisi;
    }

    public void setYatakSayisi(int yatakSayisi) {
        this.yatakSayisi = yatakSayisi;
    }

    public boolean isAcilServisVarMi() {
        return acilServisVarMi;
    }

    public void setAcilServisVarMi(boolean acilServisVarMi) {
        this.acilServisVarMi = acilServisVarMi;
    }
}
