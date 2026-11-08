package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

/**
 * {@code Person} soyut domain sinifinin JPA karsiligi. Tum kisi turlerinin
 * (hasta, calisan) ORTAK alanlarini tasir.
 * <p>
 * OOP ilkesi: domain'deki KALITIM hiyerarsisi ({@code Person} abstract),
 * JPA tarafinda {@code @MappedSuperclass} ile BIREBIR yansitilir: bu sinifin
 * kendi tablosu yoktur, alanlari alt sinif tablolarina (hastalar, calisanlar)
 * eklenir. Boylece domain modelindeki "ortak kisi bilgisi" kavrami, veritabani
 * seviyesinde de tekrar edilmeden paylasilir.
 */
@MappedSuperclass
public abstract class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "soyad", nullable = false)
    private String soyad;

    @Column(name = "tc_kimlik_no", nullable = false, unique = true, length = 11)
    private String tcKimlikNo;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon_no")
    private String telefonNo;

    @Column(name = "dogum_tarihi")
    private LocalDate dogumTarihi;

    protected PersonEntity() {
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

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public void setTcKimlikNo(String tcKimlikNo) {
        this.tcKimlikNo = tcKimlikNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonNo() {
        return telefonNo;
    }

    public void setTelefonNo(String telefonNo) {
        this.telefonNo = telefonNo;
    }

    public LocalDate getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(LocalDate dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }
}
