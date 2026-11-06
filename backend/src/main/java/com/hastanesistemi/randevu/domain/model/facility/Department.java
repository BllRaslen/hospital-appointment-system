package com.hastanesistemi.randevu.domain.model.facility;

import com.hastanesistemi.randevu.domain.model.person.Doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bir hastane/klinik icindeki bolumu (ornegin "Kardiyoloji Bolumu") temsil
 * eder. Gereksinim: "Her klinik/hastanenin ... bolumleri tanimlanabilecek."
 * <p>
 * OOP ilkesi: KAPSULLEME - doktorlar listesi disariya degistirilemez olarak
 * sunulur.
 */
public class Department {

    private Long id;
    private String ad;
    private HealthcareFacility bagliTesis;
    private final List<Doctor> doktorlar = new ArrayList<>();

    public Department(Long id, String ad, HealthcareFacility bagliTesis) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Bolum adi bos olamaz.");
        }
        this.id = id;
        this.ad = ad;
        this.bagliTesis = bagliTesis;
    }

    public void doktorEkle(Doctor doktor) {
        if (doktor != null && !doktorlar.contains(doktor)) {
            doktorlar.add(doktor);
            doktor.setDepartman(this);
        }
    }

    public List<Doctor> getDoktorlar() {
        return Collections.unmodifiableList(doktorlar);
    }

    public Long getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public HealthcareFacility getBagliTesis() {
        return bagliTesis;
    }

    public void setBagliTesis(HealthcareFacility bagliTesis) {
        this.bagliTesis = bagliTesis;
    }
}
