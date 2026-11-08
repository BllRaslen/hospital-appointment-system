package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * {@code Appointment} domain sinifinin JPA karsiligi.
 * <p>
 * {@code hasta}/{@code doktor} alanlari kasitli olarak {@code PatientEntity}/
 * {@code DoctorEntity} (somut alt siniflar) tipindedir, soyut {@code
 * PersonEntity} degil — bir randevu her zaman BELIRLI bir hasta ve BELIRLI
 * bir doktora aittir, "herhangi bir kisiye" degil. Bu, domain'deki
 * {@code Appointment}'in {@code Patient}/{@code Doctor} alanlarinin somut
 * tipte olmasiyla birebir ortusur.
 */
@Entity
@Table(name = "randevular")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hasta_id")
    private PatientEntity hasta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doktor_id")
    private DoctorEntity doktor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hizmet_id")
    private MedicalServiceEntity hizmet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tesis_id")
    private HealthcareFacilityEntity tesis;

    @Column(name = "baslangic_zamani", nullable = false)
    private LocalDateTime baslangicZamani;

    @Column(name = "bitis_zamani", nullable = false)
    private LocalDateTime bitisZamani;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", nullable = false)
    private String durum;

    @Column(name = "olusturma_zamani", nullable = false)
    private LocalDateTime olusturmaZamani;

    @Column(name = "onay_zamani")
    private LocalDateTime onayZamani;

    @Column(name = "notlar")
    private String notlar;

    protected AppointmentEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientEntity getHasta() {
        return hasta;
    }

    public void setHasta(PatientEntity hasta) {
        this.hasta = hasta;
    }

    public DoctorEntity getDoktor() {
        return doktor;
    }

    public void setDoktor(DoctorEntity doktor) {
        this.doktor = doktor;
    }

    public MedicalServiceEntity getHizmet() {
        return hizmet;
    }

    public void setHizmet(MedicalServiceEntity hizmet) {
        this.hizmet = hizmet;
    }

    public HealthcareFacilityEntity getTesis() {
        return tesis;
    }

    public void setTesis(HealthcareFacilityEntity tesis) {
        this.tesis = tesis;
    }

    public LocalDateTime getBaslangicZamani() {
        return baslangicZamani;
    }

    public void setBaslangicZamani(LocalDateTime baslangicZamani) {
        this.baslangicZamani = baslangicZamani;
    }

    public LocalDateTime getBitisZamani() {
        return bitisZamani;
    }

    public void setBitisZamani(LocalDateTime bitisZamani) {
        this.bitisZamani = bitisZamani;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) {
        this.olusturmaZamani = olusturmaZamani;
    }

    public LocalDateTime getOnayZamani() {
        return onayZamani;
    }

    public void setOnayZamani(LocalDateTime onayZamani) {
        this.onayZamani = onayZamani;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }
}
