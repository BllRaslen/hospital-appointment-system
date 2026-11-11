package com.hastanesistemi.randevu.presentation.dto;

import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.presentation.dto.doktor.DoktorYanit;
import com.hastanesistemi.randevu.presentation.dto.hasta.HastaYanit;
import com.hastanesistemi.randevu.presentation.dto.hizmet.HizmetYanit;
import com.hastanesistemi.randevu.presentation.dto.randevu.RandevuYanit;
import com.hastanesistemi.randevu.presentation.dto.tesis.TesisYanit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Presentation katmanindaki {@code ...Yanit} DTO'larinin {@code fromDomain}
 * donusumlerinin, domain nesnesinin alanlarini DOGRU esledigini dogrulayan
 * birim testleri. Bkz. infrastructure/persistence/mapper paketindeki Mapper
 * testleri (Gun 3) - ayni "donusumu dogrula" felsefesinin presentation
 * katmanindaki karsiligi.
 */
class DtoDonusumTest {

    @Test
    void hastaYaniti_tumAlanlariDogruEsler() {
        Patient hasta = new Patient(1L, "Ayse", "Yilmaz", "12345678901", "ayse@example.com",
                "+905551112233", LocalDate.of(1990, 1, 1), "A Rh+", "SGK-1");
        hasta.alerjiEkle("Penisilin");

        HastaYanit yanit = HastaYanit.fromDomain(hasta);

        assertEquals(1L, yanit.id());
        assertEquals("Ayse", yanit.ad());
        assertEquals("ayse@example.com", yanit.email());
        assertEquals("A Rh+", yanit.kanGrubu());
        assertEquals(1, yanit.alerjiler().size());
        assertEquals("Penisilin", yanit.alerjiler().get(0));
    }

    @Test
    void doktorYaniti_tumAlanlariDogruEsler() {
        Doctor doktor = new Doctor(2L, "Mehmet", "Demir", "98765432109", "mehmet@example.com",
                "+905554445566", LocalDate.of(1980, 5, 5), "DR-01", LocalDate.of(2010, 1, 1),
                BigDecimal.valueOf(50000), Specialty.KARDIYOLOJI, "DIPLOMA-1");

        DoktorYanit yanit = DoktorYanit.fromDomain(doktor);

        assertEquals(2L, yanit.id());
        assertEquals(Specialty.KARDIYOLOJI, yanit.uzmanlikAlani());
        assertEquals("DIPLOMA-1", yanit.diplomaNo());
        assertEquals(0, yanit.tamamlananRandevuSayisi());
    }

    @Test
    void hizmetYaniti_tumAlanlariDogruEsler() {
        MedicalService hizmet = new MedicalService(3L, "Genel Muayene", "Rutin kontrol", 30, BigDecimal.valueOf(500));

        HizmetYanit yanit = HizmetYanit.fromDomain(hizmet);

        assertEquals(3L, yanit.id());
        assertEquals("Genel Muayene", yanit.ad());
        assertEquals(30, yanit.sureDakika());
        assertEquals(BigDecimal.valueOf(500), yanit.ucret());
    }

    @Test
    void tesisYaniti_tesisTuruVeSehirDogruEslenir() {
        Hospital hastane = new Hospital(4L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);

        TesisYanit yanit = TesisYanit.fromDomain(hastane);

        assertEquals("HASTANE", yanit.tesisTuru());
        assertEquals("Istanbul", yanit.sehir());
        assertEquals(40, yanit.gunlukMaksimumRandevuKapasitesi());
    }

    @Test
    void randevuYaniti_efektifUcretDisaridanGelenDegeriKullanir() {
        TimeSlot zaman = new TimeSlot(LocalDateTime.of(2026, 11, 9, 10, 0), LocalDateTime.of(2026, 11, 9, 10, 30));
        Doctor doktor = new Doctor(2L, "Mehmet", "Demir", "98765432109", "mehmet@example.com",
                "+905554445566", LocalDate.of(1980, 5, 5), "DR-01", LocalDate.of(2010, 1, 1),
                BigDecimal.valueOf(50000), Specialty.KARDIYOLOJI, "DIPLOMA-1");
        doktor.uygunlukEkle(new WorkingHours(zaman.getBaslangic().getDayOfWeek(), LocalTime.of(9, 0), LocalTime.of(17, 0)));
        Patient hasta = new Patient(1L, "Ayse", "Yilmaz", "12345678901", "ayse@example.com",
                "+905551112233", LocalDate.of(1990, 1, 1), "A Rh+", "SGK-1");
        MedicalService hizmet = new MedicalService(3L, "Genel Muayene", "Rutin kontrol", 30, BigDecimal.valueOf(500));
        Hospital tesis = new Hospital(4L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);
        var randevu = new com.hastanesistemi.randevu.domain.model.appointment.Appointment(
                5L, hasta, doktor, hizmet, tesis, zaman);

        RandevuYanit yanit = RandevuYanit.fromDomain(randevu, BigDecimal.valueOf(400));

        assertEquals(5L, yanit.id());
        assertEquals("Genel Muayene", yanit.hizmetAdi());
        assertEquals("Test Hastanesi", yanit.tesisAdi());
        assertEquals(BigDecimal.valueOf(400), yanit.efektifUcret());
        assertEquals(zaman.getBaslangic(), yanit.baslangic());
    }
}
