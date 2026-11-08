package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * {@code PatientMapper}'in domain &lt;-&gt; entity donusumunu, GERCEK bir
 * veritabani veya Spring baglami (context) olmadan dogrulayan birim
 * testleri. Mapper siniflari kasitli olarak durumsuz (stateless) ve saf
 * Java oldugundan, bu testler de JPA/Spring gerektirmeden hizli calisir.
 */
class PatientMapperTest {

    @Test
    void domainEntityDomainTuruYuvarlakTuruKorur() {
        Patient hasta = new Patient(5L, "Elif", "Kara", "11223344556", "elif@example.com",
                "+905001112233", LocalDate.of(1992, 4, 15), "0 Rh-", "SGK-123");
        hasta.alerjiEkle("Penisilin");
        hasta.alerjiEkle("Polen");

        PatientEntity entity = PatientMapper.toEntity(hasta);
        assertEquals(hasta.getId(), entity.getId());
        assertEquals(hasta.getAd(), entity.getAd());
        assertEquals(hasta.getTcKimlikNo(), entity.getTcKimlikNo());
        assertEquals(hasta.getKanGrubu(), entity.getKanGrubu());
        assertEquals(hasta.getSigortaNo(), entity.getSigortaNo());
        assertIterableEquals(hasta.getAlerjiler(), entity.getAlerjiler());

        Patient yenidenKurulan = PatientMapper.toDomain(entity);
        assertEquals(hasta.getId(), yenidenKurulan.getId());
        assertEquals(hasta.getTamAd(), yenidenKurulan.getTamAd());
        assertEquals(hasta.getTcKimlikNo(), yenidenKurulan.getTcKimlikNo());
        assertEquals(hasta.getKanGrubu(), yenidenKurulan.getKanGrubu());
        assertIterableEquals(hasta.getAlerjiler(), yenidenKurulan.getAlerjiler());
    }
}
