package com.hastanesistemi.randevu.domain.model.appointment;

import com.hastanesistemi.randevu.domain.exception.InvalidAppointmentStateException;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.time.LocalDateTime;

/**
 * Bir hastanin bir doktordan belirli bir hizmeti belirli bir zaman araliginda
 * almak uzere olusturdugu randevuyu temsil eder. Gereksinim: "Randevu
 * detaylari (islem, sure, ucret, calisan, tarih/saat) kaydedilecek."
 * <p>
 * OOP ilkesi: KOMPOZISYON - Appointment, Patient/Doctor/MedicalService/
 * HealthcareFacility/TimeSlot nesnelerini REFERANS olarak icerir ("has-a"),
 * onlardan turemez ("is-a" degildir). Bu, nesneler arasi iliskiyi dogru
 * sekilde kalitim DEGIL kompozisyon ile modellememiz gerektigini gosterir.
 * <p>
 * OOP ilkesi: KAPSULLEME (en guclu ornek) - durum (AppointmentStatus) alani
 * DISARIDAN DOGRUDAN DEGISTIRILEMEZ (setter yok!). Durum degisikligi SADECE
 * onayla(), iptalEt(), tamamla() metotlariyla, ve SADECE gecerli bir durum
 * gecisiyse yapilabilir. Bu, nesnenin kendi ic tutarliligini KENDISININ
 * korumasi (bir sinifin sorumlulugu kendi verisinin gecerliligini
 * garanti etmesidir) ilkesinin somut bir uygulamasidir.
 */
public class Appointment {

    private Long id;
    private final Patient hasta;
    private final Doctor doktor;
    private final MedicalService hizmet;
    private final HealthcareFacility tesis;
    private final TimeSlot zamanAraligi;
    private AppointmentStatus durum;
    private final LocalDateTime olusturmaZamani;
    private LocalDateTime onayZamani;
    private String notlar;

    public Appointment(Long id, Patient hasta, Doctor doktor, MedicalService hizmet,
                        HealthcareFacility tesis, TimeSlot zamanAraligi) {
        if (hasta == null || doktor == null || hizmet == null || tesis == null || zamanAraligi == null) {
            throw new IllegalArgumentException("Randevu olusturmak icin hasta, doktor, hizmet, tesis ve zaman araligi zorunludur.");
        }
        this.id = id;
        this.hasta = hasta;
        this.doktor = doktor;
        this.hizmet = hizmet;
        this.tesis = tesis;
        this.zamanAraligi = zamanAraligi;
        this.durum = AppointmentStatus.BEKLEMEDE;
        this.olusturmaZamani = LocalDateTime.now();
    }

    /**
     * Bu randevunun, ayni doktora ait baska bir randevu ile zaman olarak
     * cakisip cakismadigini kontrol eder. Iptal edilmis randevular cakisma
     * sayilmaz. Gereksinim: "Randevu olusturulurken cakisma kontrolu
     * yapilacak."
     */
    public boolean cakisiyorMu(Appointment diger) {
        if (diger == null || diger.durum == AppointmentStatus.IPTAL_EDILDI || this.durum == AppointmentStatus.IPTAL_EDILDI) {
            return false;
        }
        return this.doktor.equals(diger.doktor) && this.zamanAraligi.cakisiyorMu(diger.zamanAraligi);
    }

    /** BEKLEMEDE -&gt; ONAYLANDI gecisi. Gereksinim: "Randevu icin onay mekanizmasi olacak." */
    public void onayla() {
        if (durum != AppointmentStatus.BEKLEMEDE) {
            throw new InvalidAppointmentStateException(
                    "Sadece BEKLEMEDE durumundaki bir randevu onaylanabilir. Mevcut durum: " + durum);
        }
        this.durum = AppointmentStatus.ONAYLANDI;
        this.onayZamani = LocalDateTime.now();
    }

    /** BEKLEMEDE veya ONAYLANDI -&gt; IPTAL_EDILDI gecisi. */
    public void iptalEt() {
        if (durum == AppointmentStatus.TAMAMLANDI || durum == AppointmentStatus.IPTAL_EDILDI) {
            throw new InvalidAppointmentStateException(
                    "TAMAMLANDI veya zaten IPTAL_EDILDI durumundaki bir randevu iptal edilemez. Mevcut durum: " + durum);
        }
        this.durum = AppointmentStatus.IPTAL_EDILDI;
    }

    /** ONAYLANDI -&gt; TAMAMLANDI gecisi. Doktorun ek odeme sayacini da gunceller. */
    public void tamamla() {
        if (durum != AppointmentStatus.ONAYLANDI) {
            throw new InvalidAppointmentStateException(
                    "Sadece ONAYLANDI durumundaki bir randevu tamamlanabilir. Mevcut durum: " + durum);
        }
        this.durum = AppointmentStatus.TAMAMLANDI;
        this.doktor.tamamlananRandevuSayaciniArttir();
    }

    public Long getId() {
        return id;
    }

    public Patient getHasta() {
        return hasta;
    }

    public Doctor getDoktor() {
        return doktor;
    }

    public MedicalService getHizmet() {
        return hizmet;
    }

    public HealthcareFacility getTesis() {
        return tesis;
    }

    public TimeSlot getZamanAraligi() {
        return zamanAraligi;
    }

    public AppointmentStatus getDurum() {
        return durum;
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public LocalDateTime getOnayZamani() {
        return onayZamani;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }

    @Override
    public String toString() {
        return "Randevu{hasta=" + hasta.getTamAd() + ", doktor=" + doktor.getTamAd()
                + ", hizmet=" + hizmet.getAd() + ", zaman=" + zamanAraligi + ", durum=" + durum + "}";
    }
}
