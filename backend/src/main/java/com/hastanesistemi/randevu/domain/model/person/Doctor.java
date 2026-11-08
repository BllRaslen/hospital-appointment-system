package com.hastanesistemi.randevu.domain.model.person;

import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.domain.model.notification.Notifiable;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Doktoru temsil eder. Gereksinim: "Her doktorun uzmanlik alani ve sundugu
 * hizmetler tanimlanabilecek. Doktorlarin uygunluk zaman dilimleri sisteme
 * tanimlanacak."
 * <p>
 * OOP ilkesi: KALITIM - Employee'den (dolayisiyla Person'dan) turetilmistir.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - getRol(), getYetkiAciklamasi() ve
 * aylikEkOdemeHesapla() KENDI SEKLINDE uygulanir; ayni zamanda Notifiable
 * arayuzunu Patient'tan FARKLI bir kanalla (SMS) uygular.
 * <p>
 * OOP ilkesi: KAPSULLEME - sunduguHizmetler ve uygunlukZamanlari koleksiyonlari
 * disariya degistirilemez liste olarak sunulur; eklemenin tek yolu ilgili
 * metotlardir (hizmetEkle, uygunlukEkle).
 */
public class Doctor extends Employee implements Notifiable {

    private Specialty uzmanlikAlani;
    private String diplomaNo;
    private final List<MedicalService> sunduguHizmetler = new ArrayList<>();
    private final List<WorkingHours> uygunlukZamanlari = new ArrayList<>();
    private int tamamlananRandevuSayisi = 0;

    public Doctor(Long id, String ad, String soyad, String tcKimlikNo, String email,
                   String telefonNo, LocalDate dogumTarihi, String calisanNo,
                   LocalDate iseBaslamaTarihi, BigDecimal temelMaas,
                   Specialty uzmanlikAlani, String diplomaNo) {
        super(id, ad, soyad, tcKimlikNo, email, telefonNo, dogumTarihi, calisanNo,
                iseBaslamaTarihi, temelMaas);
        this.uzmanlikAlani = uzmanlikAlani;
        this.diplomaNo = diplomaNo;
    }

    @Override
    public Role getRol() {
        return Role.DOKTOR;
    }

    @Override
    public String getYetkiAciklamasi() {
        return "Kendisine atanan randevulari onaylayabilir/tamamlayabilir ve uygunluk saatlerini yonetebilir.";
    }

    @Override
    public String bildirimGonder(String mesaj) {
        return "[SMS -> " + getTelefonNo() + "] " + mesaj;
    }

    /**
     * Doktorun ek odemesi, tamamladigi randevu sayisina bagli olarak hesaplanir.
     * OOP ilkesi: COK BICIMLILIK - Employee.aylikEkOdemeHesapla()'nin bu
     * sinifa ozgu uygulamasi.
     */
    @Override
    public BigDecimal aylikEkOdemeHesapla() {
        return BigDecimal.valueOf(tamamlananRandevuSayisi).multiply(BigDecimal.valueOf(50));
    }

    public void tamamlananRandevuSayaciniArttir() {
        this.tamamlananRandevuSayisi++;
    }

    /**
     * Veritabanindan okunan, zaten gecmiste birikmis tamamlanan randevu
     * sayisini DOGRUDAN atar. Gunluk is akisinda (bir randevu tamamlandiginda)
     * SADECE {@link #tamamlananRandevuSayaciniArttir()} kullanilmalidir; bu
     * metot yalnizca infrastructure/persistence katmaninin, veritabanindan
     * okunan bir doktoru dogru sayacla yeniden kurmasi (rehydration) icindir.
     */
    public void tamamlananRandevuSayisiniAyarla(int tamamlananRandevuSayisi) {
        if (tamamlananRandevuSayisi < 0) {
            throw new IllegalArgumentException("Tamamlanan randevu sayisi negatif olamaz.");
        }
        this.tamamlananRandevuSayisi = tamamlananRandevuSayisi;
    }

    public void hizmetEkle(MedicalService hizmet) {
        if (hizmet != null && !sunduguHizmetler.contains(hizmet)) {
            sunduguHizmetler.add(hizmet);
        }
    }

    public List<MedicalService> getSunduguHizmetler() {
        return Collections.unmodifiableList(sunduguHizmetler);
    }

    public void uygunlukEkle(WorkingHours uygunluk) {
        if (uygunluk != null) {
            uygunlukZamanlari.add(uygunluk);
        }
    }

    public List<WorkingHours> getUygunlukZamanlari() {
        return Collections.unmodifiableList(uygunlukZamanlari);
    }

    /**
     * Talep edilen zaman araliginin, doktorun tanimli uygunluk saatlerinden
     * en az biri icinde tamamen yer alip almadigini kontrol eder. Gereksinim:
     * "Hastalar sadece bu zaman araliklarina randevu alabilecek."
     */
    public boolean zamanUygunMu(TimeSlot talepEdilenZaman) {
        DayOfWeek gun = talepEdilenZaman.getBaslangic().getDayOfWeek();
        return uygunlukZamanlari.stream().anyMatch(uygunluk ->
                uygunluk.getGun() == gun
                        && !talepEdilenZaman.getBaslangic().toLocalTime().isBefore(uygunluk.getBaslangicSaati())
                        && !talepEdilenZaman.getBitis().toLocalTime().isAfter(uygunluk.getBitisSaati()));
    }

    public Specialty getUzmanlikAlani() {
        return uzmanlikAlani;
    }

    public void setUzmanlikAlani(Specialty uzmanlikAlani) {
        this.uzmanlikAlani = uzmanlikAlani;
    }

    public String getDiplomaNo() {
        return diplomaNo;
    }

    public int getTamamlananRandevuSayisi() {
        return tamamlananRandevuSayisi;
    }
}
