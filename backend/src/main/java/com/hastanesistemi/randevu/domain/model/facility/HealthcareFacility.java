package com.hastanesistemi.randevu.domain.model.facility;

import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tek bir fiziksel saglik tesisini (bir hastane binasi veya bir klinik subesi)
 * temsil eden SOYUT temel sinif. Gereksinim: "Uygulamada hastaneler veya
 * klinikler yetki cercevesinde tanimlanabilecek."
 * <p>
 * Veri modeli notu: Cok subeli kuruluslar, HER SUBENIN kendi
 * HealthcareFacility kaydina sahip olmasiyla ve bu kaydin istege bagli olarak
 * bir HealthcareOrganization'a (bagliKurulus) baglanmasiyla desteklenir.
 * Veritabaninda diger tum tablolar (Department, MedicalService, Appointment,
 * Doctor atamalari vb.) bu tesisin id'sini (hospital_id) yabanci anahtar
 * olarak tasir; boylece tum subelerin verisi AYNI veritabaninda, ama
 * birbirinden izole bir sekilde saklanir.
 * <p>
 * OOP ilkesi: KALITIM + COK BICIMLILIK - Hospital ve Clinic bu siniftan
 * turetilir ve getTesisTuru() / getGunlukMaksimumRandevuKapasitesi()
 * metotlarini kendilerine ozgu sekilde uygular. Ayni "List&lt;HealthcareFacility&gt;"
 * uzerinde gezinilip her birinin kapasitesi hesaplanabilir; hangi hesaplamanin
 * calisacagi nesnenin GERCEK (runtime) turune gore belirlenir.
 */
public abstract class HealthcareFacility {

    protected Long id;
    protected String ad;
    protected Address adres;
    protected String vergiNo;
    protected HealthcareOrganization bagliKurulus;
    protected final List<Department> bolumler = new ArrayList<>();
    protected final List<WorkingHours> calismaSaatleri = new ArrayList<>();
    protected final List<MedicalService> sunduguHizmetler = new ArrayList<>();

    protected HealthcareFacility(Long id, String ad, Address adres, String vergiNo) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Tesis adi bos olamaz.");
        }
        this.id = id;
        this.ad = ad;
        this.adres = adres;
        this.vergiNo = vergiNo;
    }

    /** OOP ilkesi: COK BICIMLILIK - "HASTANE" ya da "KLINIK" doner. */
    public abstract String getTesisTuru();

    /** OOP ilkesi: COK BICIMLILIK - tesis turune gore farkli formul. */
    public abstract int getGunlukMaksimumRandevuKapasitesi();

    public void bolumEkle(Department bolum) {
        if (bolum != null && !bolumler.contains(bolum)) {
            bolumler.add(bolum);
            bolum.setBagliTesis(this);
        }
    }

    public List<Department> getBolumler() {
        return Collections.unmodifiableList(bolumler);
    }

    public void calismaSaatiEkle(WorkingHours saat) {
        if (saat != null) {
            calismaSaatleri.add(saat);
        }
    }

    public List<WorkingHours> getCalismaSaatleri() {
        return Collections.unmodifiableList(calismaSaatleri);
    }

    public void hizmetEkle(MedicalService hizmet) {
        if (hizmet != null && !sunduguHizmetler.contains(hizmet)) {
            sunduguHizmetler.add(hizmet);
        }
    }

    public List<MedicalService> getSunduguHizmetler() {
        return Collections.unmodifiableList(sunduguHizmetler);
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

    public Address getAdres() {
        return adres;
    }

    public void setAdres(Address adres) {
        this.adres = adres;
    }

    public String getVergiNo() {
        return vergiNo;
    }

    public HealthcareOrganization getBagliKurulus() {
        return bagliKurulus;
    }

    public void setBagliKurulus(HealthcareOrganization bagliKurulus) {
        this.bagliKurulus = bagliKurulus;
    }

    @Override
    public String toString() {
        return getTesisTuru() + ": " + ad;
    }
}
