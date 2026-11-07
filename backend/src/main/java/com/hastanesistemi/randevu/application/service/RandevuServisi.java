package com.hastanesistemi.randevu.application.service;

import com.hastanesistemi.randevu.application.port.out.AppointmentRepository;
import com.hastanesistemi.randevu.application.pricing.StandartUcretlendirmeStrategy;
import com.hastanesistemi.randevu.application.pricing.UcretlendirmeStrategy;
import com.hastanesistemi.randevu.domain.exception.AppointmentConflictException;
import com.hastanesistemi.randevu.domain.exception.InvalidAvailabilityException;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

/**
 * Randevu olusturma/onaylama/iptal/tamamlama is senaryolarini (use case)
 * yuruten uygulama servisi. Gereksinim: "Randevu olusturulurken cakisma
 * kontrolu yapilacak", "Hastalar sadece bu zaman araliklarina randevu
 * alabilecek", "Randevu icin onay mekanizmasi olacak".
 * <p>
 * Bu sinif KASITLI OLARAK "ince"dir (thin): gercek is kurallari (durum
 * gecisleri, cakisma hesabi, uygunluk kontrolu) domain nesnelerinin
 * ({@code Appointment}, {@code Doctor}, {@code TimeSlot}) kendi
 * metotlarindadir. Servisin sorumlulugu sadece bu domain metotlarini DOGRU
 * SIRAYLA cagirmak ve kalici hale getirme (persistence) + bildirim gibi
 * yan-etkileri (side effect) koordine etmektir.
 * <p>
 * OOP ilkesi: SOYUTLAMA (DIP - Dependency Inversion) - bu servis, bir
 * veritabanina DEGIL, {@link AppointmentRepository} PORT arayuzune bagimlidir.
 * OOP ilkesi: STRATEGY DESENI - ucret hesabi, enjekte edilen
 * {@link UcretlendirmeStrategy} uzerinden yapilir; servis HANGI
 * indirim/ucretlendirme kuralinin calisacagini bilmek zorunda degildir.
 * OOP ilkesi: COK BICIMLILIK (interface) - onay/iptal bildirimleri
 * {@code Notifiable.bildirimGonder(...)} uzerinden, alicinin gercek
 * (hasta/doktor) turune gore farkli kanaldan (e-posta/SMS) gonderilir.
 */
public class RandevuServisi {

    private final AppointmentRepository randevuDeposu;
    private final UcretlendirmeStrategy ucretlendirmeStrategy;

    public RandevuServisi(AppointmentRepository randevuDeposu) {
        this(randevuDeposu, new StandartUcretlendirmeStrategy());
    }

    public RandevuServisi(AppointmentRepository randevuDeposu, UcretlendirmeStrategy ucretlendirmeStrategy) {
        if (randevuDeposu == null) {
            throw new IllegalArgumentException("Randevu deposu (repository) bos olamaz.");
        }
        if (ucretlendirmeStrategy == null) {
            throw new IllegalArgumentException("Ucretlendirme stratejisi bos olamaz.");
        }
        this.randevuDeposu = randevuDeposu;
        this.ucretlendirmeStrategy = ucretlendirmeStrategy;
    }

    /**
     * Yeni bir randevu olusturur. Sirasiyla: (1) doktorun tanimli uygunluk
     * saatlerinden biriyle eslesip eslesmedigini, (2) doktorun ayni zaman
     * araliginda baska bir aktif randevusu olup olmadigini kontrol eder.
     */
    public Appointment randevuOlustur(Patient hasta, Doctor doktor, MedicalService hizmet,
                                       HealthcareFacility tesis, TimeSlot zamanAraligi) {
        if (!doktor.zamanUygunMu(zamanAraligi)) {
            throw new InvalidAvailabilityException(
                    "Talep edilen zaman araligi, doktorun tanimli uygunluk saatleriyle eslesmiyor: " + zamanAraligi);
        }

        Appointment aday = new Appointment(null, hasta, doktor, hizmet, tesis, zamanAraligi);
        boolean cakismaVar = randevuDeposu.doktorunRandevulariniGetir(doktor).stream()
                .anyMatch(mevcutRandevu -> mevcutRandevu.cakisiyorMu(aday));
        if (cakismaVar) {
            throw new AppointmentConflictException(
                    "Doktorun bu zaman araliginda baska bir randevusu var: " + zamanAraligi);
        }

        return randevuDeposu.kaydet(aday);
    }
}
