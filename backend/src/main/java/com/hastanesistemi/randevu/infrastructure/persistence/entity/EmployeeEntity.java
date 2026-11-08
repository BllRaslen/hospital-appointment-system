package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * {@code Employee} soyut domain sinifinin JPA karsiligi.
 * <p>
 * OOP ilkesi: domain'deki {@code Person -> Employee -> (Doctor|Staff|
 * FacilityManager)} COK SEVIYELI KALITIMI, burada {@code SINGLE_TABLE}
 * stratejisiyle birebir yansitilir: {@code DoctorEntity}/{@code StaffEntity}/
 * {@code FacilityManagerEntity} HEPSI "calisanlar" tablosunda tutulur,
 * {@code calisan_turu} ayirici (discriminator) kolonu HANGI alt turun
 * kaydi oldugunu belirtir. Bu, veritabani seviyesinde cok biciminligin
 * (polymorphism) nasil modellenecegine dair klasik bir ornektir — tek bir
 * sorguyla ("SELECT * FROM calisanlar") TUM calisan turlerine erisilebilir.
 */
@Entity
@Table(name = "calisanlar")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "calisan_turu")
public abstract class EmployeeEntity extends PersonEntity {

    @Column(name = "calisan_no", nullable = false, unique = true)
    private String calisanNo;

    @Column(name = "ise_baslama_tarihi")
    private LocalDate iseBaslamaTarihi;

    @Column(name = "temel_maas", nullable = false)
    private BigDecimal temelMaas;

    protected EmployeeEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public String getCalisanNo() {
        return calisanNo;
    }

    public void setCalisanNo(String calisanNo) {
        this.calisanNo = calisanNo;
    }

    public LocalDate getIseBaslamaTarihi() {
        return iseBaslamaTarihi;
    }

    public void setIseBaslamaTarihi(LocalDate iseBaslamaTarihi) {
        this.iseBaslamaTarihi = iseBaslamaTarihi;
    }

    public BigDecimal getTemelMaas() {
        return temelMaas;
    }

    public void setTemelMaas(BigDecimal temelMaas) {
        this.temelMaas = temelMaas;
    }
}
