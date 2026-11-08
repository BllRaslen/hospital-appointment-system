package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Patient} domain sinifinin JPA karsiligi. Bkz. {@link PersonEntity}.
 */
@Entity
@Table(name = "hastalar")
public class PatientEntity extends PersonEntity {

    @Column(name = "kan_grubu")
    private String kanGrubu;

    @Column(name = "sigorta_no")
    private String sigortaNo;

    @ElementCollection
    @CollectionTable(name = "hasta_alerjileri", joinColumns = @JoinColumn(name = "hasta_id"))
    @Column(name = "alerji")
    private List<String> alerjiler = new ArrayList<>();

    protected PatientEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    public String getSigortaNo() {
        return sigortaNo;
    }

    public void setSigortaNo(String sigortaNo) {
        this.sigortaNo = sigortaNo;
    }

    public List<String> getAlerjiler() {
        return alerjiler;
    }

    public void setAlerjiler(List<String> alerjiler) {
        this.alerjiler = alerjiler;
    }
}
