package com.hastanesistemi.randevu.domain.model.service;

import com.hastanesistemi.randevu.domain.model.facility.Department;

import java.math.BigDecimal;

/**
 * Kurumun sundugu bir saglik hizmetini (ornegin "Genel Muayene", "Radyoloji",
 * "Laboratuvar") temsil eder. Gereksinim: "Kurumun sundugu saglik hizmetleri,
 * her hizmetin suresi ve ucreti detayli sekilde tanimlanabilecek."
 * <p>
 * OOP ilkesi: KAPSULLEME - sureDakika ve ucret alanlari sadece gecerli
 * (pozitif / negatif olmayan) degerlerle set edilebilir; dogrulama kurali
 * setter icinde merkezilestirilmistir.
 */
public class MedicalService {

    private Long id;
    private String ad;
    private String aciklama;
    private int sureDakika;
    private BigDecimal ucret;
    private Department ilgiliBolum;

    public MedicalService(Long id, String ad, String aciklama, int sureDakika, BigDecimal ucret) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Hizmet adi bos olamaz.");
        }
        this.id = id;
        this.ad = ad;
        this.aciklama = aciklama;
        setSureDakika(sureDakika);
        setUcret(ucret);
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
        if (sureDakika <= 0) {
            throw new IllegalArgumentException("Hizmet suresi pozitif olmalidir.");
        }
        this.sureDakika = sureDakika;
    }

    public BigDecimal getUcret() {
        return ucret;
    }

    public void setUcret(BigDecimal ucret) {
        if (ucret == null || ucret.signum() < 0) {
            throw new IllegalArgumentException("Ucret negatif olamaz.");
        }
        this.ucret = ucret;
    }

    public Department getIlgiliBolum() {
        return ilgiliBolum;
    }

    public void setIlgiliBolum(Department ilgiliBolum) {
        this.ilgiliBolum = ilgiliBolum;
    }

    @Override
    public String toString() {
        return ad + " (" + sureDakika + " dk, " + ucret + " TL)";
    }
}
