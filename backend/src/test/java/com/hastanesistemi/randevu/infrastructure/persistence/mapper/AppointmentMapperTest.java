package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.appointment.AppointmentStatus;
import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.AppointmentEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@code AppointmentMapper}'in domain &lt;-&gt; entity donusumunu, GERCEK
 * bir veritabani olmadan dogrulayan birim testleri. Cross-aggregate
 * referanslar ({@code hasta}/{@code doktor}/{@code hizmet}/{@code tesis}),
 * bir adapter/repository olmadan DOGRUDAN diger mapper'larla uretilmistir —
 * tam {@code AppointmentRepositoryAdapter} akisini degil, SADECE mapper
 * mantigini test eder.
 */
class AppointmentMapperTest {

    @Test
    void depodanYenidenOlusturulanRandevununDurumuVeZamanlariKorunur() {
        Patient hasta = new Patient(1L, "Mehmet", "Demir", "98765432109", "mehmet@example.com",
                "+905554445566", LocalDate.of(1990, 5, 20), "A Rh+", "SGK-999");
        Doctor doktor = new Doctor(1L, "Ayse", "Yilmaz", "12345678901", "ayse@example.com",
                "+905551112233", LocalDate.of(1985, 3, 10), "DR-001",
                LocalDate.of(2015, 1, 1), BigDecimal.valueOf(60000), Specialty.KARDIYOLOJI, "DIPLOMA-123");
        MedicalService hizmet = new MedicalService(1L, "Genel Muayene", "Rutin kontrol", 30, BigDecimal.valueOf(500));
        Hospital tesis = new Hospital(1L, "Test Hastanesi",
                new Address("Istanbul", "Kadikoy", "Test Sok. No:1", "34000"), "111", 50, true);
        TimeSlot zaman = new TimeSlot(LocalDateTime.of(2026, 11, 9, 10, 0), LocalDateTime.of(2026, 11, 9, 10, 30));

        Appointment randevu = new Appointment(10L, hasta, doktor, hizmet, tesis, zaman);
        randevu.onayla();

        PatientEntity hastaEntity = PatientMapper.toEntity(hasta);
        DoctorEntity doktorEntity = DoctorMapper.toEntity(doktor, List.of());
        MedicalServiceEntity hizmetEntity = MedicalServiceMapper.toEntity(hizmet);
        HealthcareFacilityEntity tesisEntity = HealthcareFacilityMapper.toEntity(tesis, List.of());

        AppointmentEntity entity = AppointmentMapper.toEntity(randevu, hastaEntity, doktorEntity, hizmetEntity, tesisEntity);
        assertEquals(AppointmentStatus.ONAYLANDI.name(), entity.getDurum());
        assertEquals(zaman.getBaslangic(), entity.getBaslangicZamani());
        assertEquals(zaman.getBitis(), entity.getBitisZamani());

        Appointment yenidenKurulan = AppointmentMapper.toDomain(entity);
        assertEquals(AppointmentStatus.ONAYLANDI, yenidenKurulan.getDurum());
        assertEquals(randevu.getOlusturmaZamani(), yenidenKurulan.getOlusturmaZamani());
        assertEquals(randevu.getOnayZamani(), yenidenKurulan.getOnayZamani());
        assertEquals(randevu.getHasta().getTcKimlikNo(), yenidenKurulan.getHasta().getTcKimlikNo());
        assertEquals(randevu.getDoktor().getTcKimlikNo(), yenidenKurulan.getDoktor().getTcKimlikNo());
    }
}
