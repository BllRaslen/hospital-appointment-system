package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.facility.Clinic;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.ClinicEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HospitalEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code HealthcareFacilityMapper}'in, tesisin GERCEK (runtime) turune gore
 * (Hospital/Clinic) dogru somut entity/domain sinifini urettigini dogrulayan
 * birim testleri — bu, mapper seviyesinde COK BICIMLILIGIN kanitidir.
 */
class HealthcareFacilityMapperTest {

    @Test
    void hospitalDomainEntityDomainTuruYuvarlakTuruKorur() {
        Hospital hastane = new Hospital(1L, "Merkez Hastanesi",
                new Address("Ankara", "Cankaya", "Ataturk Bulvari No:10", "06100"), "999", 150, true);

        HealthcareFacilityEntity entity = HealthcareFacilityMapper.toEntity(hastane, List.of());
        assertInstanceOf(HospitalEntity.class, entity);
        assertEquals(150, ((HospitalEntity) entity).getYatakSayisi());
        assertTrue(((HospitalEntity) entity).isAcilServisVarMi());
        assertEquals("Ankara", entity.getAdres().getSehir());

        HealthcareFacility yenidenKurulan = HealthcareFacilityMapper.toDomain(entity);
        assertInstanceOf(Hospital.class, yenidenKurulan);
        assertEquals("HASTANE", yenidenKurulan.getTesisTuru());
        assertEquals(hastane.getAd(), yenidenKurulan.getAd());
    }

    @Test
    void clinicDomainEntityDomainTuruYuvarlakTuruKorur() {
        Clinic klinik = new Clinic(2L, "Yildiz Poliklinigi",
                new Address("Sakarya", "Serdivan", "Cumhuriyet Cad. No:5", "54050"), "123", 4);

        HealthcareFacilityEntity entity = HealthcareFacilityMapper.toEntity(klinik, List.of());
        assertInstanceOf(ClinicEntity.class, entity);
        assertEquals(4, ((ClinicEntity) entity).getMuayeneOdasiSayisi());

        HealthcareFacility yenidenKurulan = HealthcareFacilityMapper.toDomain(entity);
        assertInstanceOf(Clinic.class, yenidenKurulan);
        assertEquals("KLINIK", yenidenKurulan.getTesisTuru());
        assertEquals(60, yenidenKurulan.getGunlukMaksimumRandevuKapasitesi());
    }
}
