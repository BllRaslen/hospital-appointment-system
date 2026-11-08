package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * {@code MedicalService} domain sinifinin JPA karsiligi. Bkz.
 * {@link PersonEntity} ic dokumantasyonu (domain'in JPA'dan bagimsizligi) —
 * ayni gerekce burada da gecerlidir.
 */
@Entity
@Table(name = "saglik_hizmetleri")
public class MedicalServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "sure_dakika", nullable = false)
    private int sureDakika;

    @Column(name = "ucret", nullable = false)
    private BigDecimal ucret;

    protected MedicalServiceEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public int getSureDakika() {
        return sureDakika;
    }

    public void setSureDakika(int sureDakika) {
        this.sureDakika = sureDakika;
    }

    public BigDecimal getUcret() {
        return ucret;
    }

    public void setUcret(BigDecimal ucret) {
        this.ucret = ucret;
    }
}
