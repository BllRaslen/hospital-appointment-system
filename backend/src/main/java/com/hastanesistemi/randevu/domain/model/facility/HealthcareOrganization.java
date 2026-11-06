package com.hastanesistemi.randevu.domain.model.facility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Birden fazla subeyi (Hospital/Clinic) catisi altinda toplayan sağlık
 * kuruluşunu temsil eder. Gereksinim: "Uygulama hem tek bir klinik/hastane
 * icin hem de cok subeli saglik kuruluslarini destekleyecek sekilde
 * calisabilir olmalidir."
 * <p>
 * Tek subeli bir klinik icin bu sinifa hic gerek yoktur (HealthcareFacility
 * tek basina kullanilir, bagliKurulus alani null kalir). Cok subeli bir
 * zincir icin, her sube ayni HealthcareOrganization'a baglanir; bu sayede
 * "kurum yoneticisi" tum subeleri, "sube yoneticisi" (FacilityManager) ise
 * sadece kendi subesini gorur/yonetir.
 * <p>
 * OOP ilkesi: KOMPOZISYON (composition / "has-a" iliskisi) - bir
 * HealthcareOrganization, birden fazla HealthcareFacility'yi ICERIR. Bu,
 * kalitimdan ("is-a") farkli bir OOP iliski turudur ve nesneler arasi
 * iliskileri modellemenin onemli bir yoludur.
 */
public class HealthcareOrganization {

    private Long id;
    private String kurulusAdi;
    private final List<HealthcareFacility> subeler = new ArrayList<>();

    public HealthcareOrganization(Long id, String kurulusAdi) {
        if (kurulusAdi == null || kurulusAdi.isBlank()) {
            throw new IllegalArgumentException("Kurulus adi bos olamaz.");
        }
        this.id = id;
        this.kurulusAdi = kurulusAdi;
    }

    public void subeEkle(HealthcareFacility sube) {
        if (sube != null && !subeler.contains(sube)) {
            subeler.add(sube);
            sube.setBagliKurulus(this);
        }
    }

    public List<HealthcareFacility> getSubeler() {
        return Collections.unmodifiableList(subeler);
    }

    public int toplamSubeSayisi() {
        return subeler.size();
    }

    public Long getId() {
        return id;
    }

    public String getKurulusAdi() {
        return kurulusAdi;
    }

    public void setKurulusAdi(String kurulusAdi) {
        this.kurulusAdi = kurulusAdi;
    }
}
