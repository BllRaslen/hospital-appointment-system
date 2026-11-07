package com.hastanesistemi.randevu.application;

import com.hastanesistemi.randevu.application.service.RandevuServisi;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.appointment.AppointmentStatus;
import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code RandevuServisi} use-case'lerini, GERCEK bir veritabani olmadan,
 * {@link SahteRandevuDeposu} (bellek ici sahte repository) kullanarak
 * dogrulayan birim testleri. Bu, Repository deseninin (port/adapter) ve
 * Dependency Inversion'in saglandigi en net kanittir: application katmani,
 * infrastructure/PostgreSQL olmadan test edilebilir.
 */
class RandevuServisiTest {

    private Doctor doktorOlustur(DayOfWeek uygunGun) {
        Doctor doktor = new Doctor(1L, "Ayse", "Yilmaz", "12345678901", "ayse@example.com",
                "+905551112233", LocalDate.of(1985, 3, 10), "DR-001",
                LocalDate.of(2015, 1, 1), BigDecimal.valueOf(60000),
                Specialty.KARDIYOLOJI, "DIPLOMA-123");
        doktor.uygunlukEkle(new WorkingHours(uygunGun, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        return doktor;
    }

    private Patient hastaOlustur(String sigortaNo) {
        return new Patient(1L, "Mehmet", "Demir", "98765432109", "mehmet@example.com",
                "+905554445566", LocalDate.of(1990, 5, 20), "A Rh+", sigortaNo);
    }

    private MedicalService hizmetOlustur() {
        return new MedicalService(1L, "Genel Muayene", "Rutin kontrol", 30, BigDecimal.valueOf(500));
    }

    private Hospital tesisOlustur() {
        return new Hospital(1L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);
    }

    @Test
    void randevuOlusturmaBasarili_doktorUygunlugunaUyanZamanIcin() {
        TimeSlot zaman = new TimeSlot(LocalDateTime.of(2026, 11, 9, 10, 0), LocalDateTime.of(2026, 11, 9, 10, 30));
        Doctor doktor = doktorOlustur(zaman.getBaslangic().getDayOfWeek());
        Patient hasta = hastaOlustur("SGK-999");

        RandevuServisi servis = new RandevuServisi(new SahteRandevuDeposu());
        Appointment randevu = servis.randevuOlustur(hasta, doktor, hizmetOlustur(), tesisOlustur(), zaman);

        assertEquals(AppointmentStatus.BEKLEMEDE, randevu.getDurum());
        assertTrue(randevu.getId() > 0, "Repository, kaydedilen randevuya bir id atamis olmalidir.");
    }
}
