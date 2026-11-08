package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code DoctorMapper}'in domain &lt;-&gt; entity donusumunu dogrulayan,
 * Spring/JPA gerektirmeyen birim testleri. Cross-aggregate referanslar
 * (sunduguHizmetler), gercek bir adapter/repository olmadan, DOGRUDAN
 * verilen entity listesiyle test edilir.
 */
class DoctorMapperTest {

    @Test
    void domainEntityDomainTuruYuvarlakTuruKorur() {
        Doctor doktor = new Doctor(7L, "Burak", "Sahin", "33344455566", "burak@example.com",
                "+905002223344", LocalDate.of(1980, 6, 1), "DR-042",
                LocalDate.of(2012, 9, 1), BigDecimal.valueOf(75000), Specialty.ORTOPEDI, "DIPLOMA-777");
        doktor.uygunlukEkle(new WorkingHours(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0)));
        doktor.tamamlananRandevuSayaciniArttir();
        doktor.tamamlananRandevuSayaciniArttir();

        MedicalServiceEntity hizmetEntity = new MedicalServiceEntity();
        hizmetEntity.setId(3L);
        hizmetEntity.setAd("Fizik Tedavi");
        hizmetEntity.setSureDakika(45);
        hizmetEntity.setUcret(BigDecimal.valueOf(800));

        DoctorEntity entity = DoctorMapper.toEntity(doktor, List.of(hizmetEntity));
        assertEquals(doktor.getId(), entity.getId());
        assertEquals(doktor.getUzmanlikAlani().name(), entity.getUzmanlikAlani());
        assertEquals(doktor.getTamamlananRandevuSayisi(), entity.getTamamlananRandevuSayisi());
        assertEquals(1, entity.getUygunlukZamanlari().size());
        assertEquals(1, entity.getSunduguHizmetler().size());

        Doctor yenidenKurulan = DoctorMapper.toDomain(entity);
        assertEquals(doktor.getUzmanlikAlani(), yenidenKurulan.getUzmanlikAlani());
        assertEquals(doktor.getTamamlananRandevuSayisi(), yenidenKurulan.getTamamlananRandevuSayisi());
        assertEquals(1, yenidenKurulan.getUygunlukZamanlari().size());
        assertEquals(1, yenidenKurulan.getSunduguHizmetler().size());
        assertTrue(yenidenKurulan.getSunduguHizmetler().stream()
                .anyMatch(h -> h.getAd().equals("Fizik Tedavi")));

        MedicalService eslesenHizmet = yenidenKurulan.getSunduguHizmetler().get(0);
        assertEquals(hizmetEntity.getId(), eslesenHizmet.getId());
    }
}
