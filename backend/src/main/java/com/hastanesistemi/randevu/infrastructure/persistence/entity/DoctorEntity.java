package com.hastanesistemi.randevu.infrastructure.persistence.entity;

import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.WorkingHoursEmbeddable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Doctor} domain sinifinin JPA karsiligi. Bkz. {@link EmployeeEntity}
 * (SINGLE_TABLE kalitim stratejisi).
 */
@Entity
@DiscriminatorValue("DOKTOR")
public class DoctorEntity extends EmployeeEntity {

    // Bilerek String: Specialty<->String donusumu DoctorMapper'da yapilir
    // (bkz. DoctorMapper). "@Enumerated" BURAYA EKLENMEMELI - Hibernate bu
    // anotasyonu sadece gercek enum alanlarinda kabul eder.
    @Column(name = "uzmanlik_alani")
    private String uzmanlikAlani;

    @Column(name = "diploma_no")
    private String diplomaNo;

    @Column(name = "tamamlanan_randevu_sayisi", nullable = false)
    private int tamamlananRandevuSayisi = 0;

    @ManyToMany
    @JoinTable(
            name = "doktor_hizmetleri",
            joinColumns = @JoinColumn(name = "doktor_id"),
            inverseJoinColumns = @JoinColumn(name = "hizmet_id"))
    private List<MedicalServiceEntity> sunduguHizmetler = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "doktor_uygunluklari", joinColumns = @JoinColumn(name = "doktor_id"))
    private List<WorkingHoursEmbeddable> uygunlukZamanlari = new ArrayList<>();

    public DoctorEntity() {
        // JPA icin zorunlu bos constructor.
    }

    public String getUzmanlikAlani() {
        return uzmanlikAlani;
    }

    public void setUzmanlikAlani(String uzmanlikAlani) {
        this.uzmanlikAlani = uzmanlikAlani;
    }

    public String getDiplomaNo() {
        return diplomaNo;
    }

    public void setDiplomaNo(String diplomaNo) {
        this.diplomaNo = diplomaNo;
    }

    public int getTamamlananRandevuSayisi() {
        return tamamlananRandevuSayisi;
    }

    public void setTamamlananRandevuSayisi(int tamamlananRandevuSayisi) {
        this.tamamlananRandevuSayisi = tamamlananRandevuSayisi;
    }

    public List<MedicalServiceEntity> getSunduguHizmetler() {
        return sunduguHizmetler;
    }

    public void setSunduguHizmetler(List<MedicalServiceEntity> sunduguHizmetler) {
        this.sunduguHizmetler = sunduguHizmetler;
    }

    public List<WorkingHoursEmbeddable> getUygunlukZamanlari() {
        return uygunlukZamanlari;
    }

    public void setUygunlukZamanlari(List<WorkingHoursEmbeddable> uygunlukZamanlari) {
        this.uygunlukZamanlari = uygunlukZamanlari;
    }
}
