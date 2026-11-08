package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * {@code FacilityManager} domain sinifinin JPA karsiligi. Bkz.
 * {@link EmployeeEntity} (SINGLE_TABLE kalitim stratejisi).
 */
@Entity
@DiscriminatorValue("YONETICI")
public class FacilityManagerEntity extends EmployeeEntity {

    @ManyToOne
    @JoinColumn(name = "yonettigi_tesis_id")
    private HealthcareFacilityEntity yonettigiTesis;

    public FacilityManagerEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public HealthcareFacilityEntity getYonettigiTesis() {
        return yonettigiTesis;
    }

    public void setYonettigiTesis(HealthcareFacilityEntity yonettigiTesis) {
        this.yonettigiTesis = yonettigiTesis;
    }
}
