package com.hastanesistemi.randevu.domain;

import com.hastanesistemi.randevu.domain.exception.InvalidAppointmentStateException;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.appointment.AppointmentStatus;
import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.Clinic;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.domain.model.notification.Notifiable;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Employee;
import com.hastanesistemi.randevu.domain.model.person.FacilityManager;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Role;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.person.Staff;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain modelinin temel is kurallarini ve OOP davranislarini dogrulayan
 * birim testleri. Bu testler, Clean Architecture'in en buyuk avantajini
 * gosterir: domain katmani Spring/veritabani olmadan, saf Java nesneleriyle
 * test edilebilir (hizli ve izole testler).
 */
class AppointmentDomainTest {

    private Doctor ornekDoktorOlustur() {
        return new Doctor(1L, "Ayse", "Yilmaz", "12345678901", "ayse@example.com",
                "+905551112233", LocalDate.of(1985, 3, 10), "DR-001",
                LocalDate.of(2015, 1, 1), BigDecimal.valueOf(60000),
                Specialty.KARDIYOLOJI, "DIPLOMA-123");
    }

    private Patient ornekHastaOlustur() {
        return new Patient(1L, "Mehmet", "Demir", "98765432109", "mehmet@example.com",
                "+905554445566", LocalDate.of(1990, 5, 20), "A Rh+", "SGK-999");
    }

    private MedicalService ornekHizmetOlustur() {
        return new MedicalService(1L, "Genel Muayene", "Rutin kontrol", 30, BigDecimal.valueOf(500));
    }

    @Test
    void cokBicimlilik_calisanTurlerineGoreFarkliEkOdemeHesaplar() {
        Doctor doktor = ornekDoktorOlustur();
        doktor.tamamlananRandevuSayaciniArttir();
        doktor.tamamlananRandevuSayaciniArttir();

        Staff hemsire = new Staff(2L, "Fatma", "Kaya", "11122233344", "fatma@example.com",
                "+905557778899", LocalDate.of(1992, 2, 2), "ST-001",
                LocalDate.of(2020, 1, 1), BigDecimal.valueOf(30000), "Hemsire");

        FacilityManager yonetici = new FacilityManager(3L, "Ali", "Can", "55566677788",
                "ali@example.com", "+905551231231", LocalDate.of(1980, 1, 1), "YN-001",
                LocalDate.of(2010, 1, 1), BigDecimal.valueOf(80000));

        // Ayni tip (List<Employee>) uzerinde, her nesnenin GERCEK turune gore
        // farkli aylikEkOdemeHesapla() uygulamasi calisir (polimorfizm).
        List<Employee> calisanlar = List.of(doktor, hemsire, yonetici);

        assertEquals(BigDecimal.valueOf(100), calisanlar.get(0).aylikEkOdemeHesapla());
        assertEquals(BigDecimal.valueOf(500), calisanlar.get(1).aylikEkOdemeHesapla());
        assertEquals(BigDecimal.valueOf(8000.00).setScale(2), calisanlar.get(2).aylikEkOdemeHesapla().setScale(2));

        assertEquals(Role.DOKTOR, doktor.getRol());
        assertEquals(Role.SAGLIK_PERSONELI, hemsire.getRol());
        assertEquals(Role.YONETICI, yonetici.getRol());
    }

    @Test
    void cokBicimlilik_tesisTuruneGoreFarkliKapasiteHesaplar() {
        Hospital hastane = new Hospital(1L, "Sakarya Devlet Hastanesi",
                new Address("Sakarya", "Adapazari", "Merkez Cad. No:1", "54000"), "1234567890", 200, true);
        Clinic klinik = new Clinic(2L, "Yildiz Poliklinigi",
                new Address("Sakarya", "Serdivan", "Cumhuriyet Cad. No:5", "54050"), "0987654321", 3);

        assertEquals("HASTANE", hastane.getTesisTuru());
        assertEquals("KLINIK", klinik.getTesisTuru());
        assertEquals(40, hastane.getGunlukMaksimumRandevuKapasitesi());
        assertEquals(45, klinik.getGunlukMaksimumRandevuKapasitesi());
    }

    @Test
    void cokBicimlilik_notifiableArayuzuFarkliKanallardanBildirimGonderir() {
        Doctor doktor = ornekDoktorOlustur();
        Patient hasta = ornekHastaOlustur();

        List<Notifiable> bildirimAlacaklar = List.of(hasta, doktor);

        assertTrue(bildirimAlacaklar.get(0).bildirimGonder("Randevunuz onaylandi.").startsWith("[E-POSTA"));
        assertTrue(bildirimAlacaklar.get(1).bildirimGonder("Yeni randevunuz var.").startsWith("[SMS"));
    }

    @Test
    void randevuCakismaKontroluDogruCalisir() {
        Doctor doktor = ornekDoktorOlustur();
        Patient hasta1 = ornekHastaOlustur();
        Patient hasta2 = new Patient(2L, "Zeynep", "Aydin", "22233344455", "zeynep@example.com",
                null, LocalDate.of(1995, 7, 7), null, null);
        MedicalService hizmet = ornekHizmetOlustur();
        Hospital hastane = new Hospital(1L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);

        LocalDateTime baslangic = LocalDateTime.of(2026, 11, 10, 10, 0);
        TimeSlot zaman1 = new TimeSlot(baslangic, baslangic.plusMinutes(30));
        TimeSlot zaman2 = new TimeSlot(baslangic.plusMinutes(15), baslangic.plusMinutes(45));

        Appointment randevu1 = new Appointment(1L, hasta1, doktor, hizmet, hastane, zaman1);
        Appointment randevu2 = new Appointment(2L, hasta2, doktor, hizmet, hastane, zaman2);

        assertTrue(randevu1.cakisiyorMu(randevu2));

        randevu1.iptalEt();
        assertFalse(randevu1.cakisiyorMu(randevu2));
    }

    @Test
    void randevuDurumGecisleriKapsullenmistir() {
        Doctor doktor = ornekDoktorOlustur();
        Patient hasta = ornekHastaOlustur();
        MedicalService hizmet = ornekHizmetOlustur();
        Hospital hastane = new Hospital(1L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);
        TimeSlot zaman = new TimeSlot(LocalDateTime.of(2026, 11, 10, 10, 0), LocalDateTime.of(2026, 11, 10, 10, 30));

        Appointment randevu = new Appointment(1L, hasta, doktor, hizmet, hastane, zaman);
        assertEquals(AppointmentStatus.BEKLEMEDE, randevu.getDurum());

        // Onaylanmamis bir randevu direkt tamamlanamaz.
        assertThrows(InvalidAppointmentStateException.class, randevu::tamamla);

        randevu.onayla();
        assertEquals(AppointmentStatus.ONAYLANDI, randevu.getDurum());

        randevu.tamamla();
        assertEquals(AppointmentStatus.TAMAMLANDI, randevu.getDurum());
        assertEquals(1, doktor.getTamamlananRandevuSayisi());

        // Tamamlanmis bir randevu iptal edilemez.
        assertThrows(InvalidAppointmentStateException.class, randevu::iptalEt);
    }
}
