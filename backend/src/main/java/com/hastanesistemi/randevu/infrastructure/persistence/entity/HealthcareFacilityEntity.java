package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.WorkingHoursEmbeddable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code HealthcareFacility} soyut domain sinifinin JPA karsiligi.
 * <p>
 * OOP ilkesi: Bkz. {@link EmployeeEntity} ic dokumantasyonu — ayni
 * SINGLE_TABLE + discriminator deseni, burada {@code Hospital}/{@code Clinic}
 * kalitim hiyerarsisini yansitmak icin tekrar kullanilir.
 * <p>
 * Bilinen sinirlama (kasitli kapsam disi): {@code bolumler} (Department) ve
 * {@code bagliKurulus} (HealthcareOrganization) iliskileri bu asamada
 * eslenmemistir — application katmaninda bu iki varlik icin henuz bir
 * repository portu tanimlanmadigindan (sadece Patient/Doctor/MedicalService/
 * HealthcareFacility/Appointment portlari var), eslemeleri simdilik
 * gereksiz karmasiklik eklerdi. Department/HealthcareOrganization portlari
 * eklendiginde buraya tasinacaktir.
 */
@Entity
@Table(name = "tesisler")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tesis_turu")
public abstract class HealthcareFacilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false)
    private String ad;

    @Embedded
    private AddressEmbeddable adres;

    @Column(name = "vergi_no")
    private String vergiNo;

    @ElementCollection
    @CollectionTable(name = "tesis_calisma_saatleri", joinColumns = @JoinColumn(name = "tesis_id"))
    private List<WorkingHoursEmbeddable> calismaSaatleri = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tesis_hizmetleri",
            joinColumns = @JoinColumn(name = "tesis_id"),
            inverseJoinColumns = @JoinColumn(name = "hizmet_id"))
    private List<MedicalServiceEntity> sunduguHizmetler = new ArrayList<>();

    protected HealthcareFacilityEntity() {
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

    public AddressEmbeddable getAdres() {
        return adres;
    }

    public void setAdres(AddressEmbeddable adres) {
        this.adres = adres;
    }

    public String getVergiNo() {
        return vergiNo;
    }

    public void setVergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
    }

    public List<WorkingHoursEmbeddable> getCalismaSaatleri() {
        return calismaSaatleri;
    }

    public void setCalismaSaatleri(List<WorkingHoursEmbeddable> calismaSaatleri) {
        this.calismaSaatleri = calismaSaatleri;
    }

    public List<MedicalServiceEntity> getSunduguHizmetler() {
        return sunduguHizmetler;
    }

    public void setSunduguHizmetler(List<MedicalServiceEntity> sunduguHizmetler) {
        this.sunduguHizmetler = sunduguHizmetler;
    }
}
