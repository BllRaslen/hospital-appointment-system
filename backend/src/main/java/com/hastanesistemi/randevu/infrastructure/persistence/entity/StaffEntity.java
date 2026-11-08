package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * {@code Staff} domain sinifinin JPA karsiligi. Bkz. {@link EmployeeEntity}
 * (SINGLE_TABLE kalitim stratejisi).
 */
@Entity
@DiscriminatorValue("SAGLIK_PERSONELI")
public class StaffEntity extends EmployeeEntity {

    @Column(name = "unvan")
    private String unvan;

    public StaffEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }
}
