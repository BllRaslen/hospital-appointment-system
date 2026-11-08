package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;

import java.util.ArrayList;

/**
 * {@code Patient} (domain) ile {@code PatientEntity} (JPA) arasindaki
 * donusumu yapan, durumsuz (stateless) yardimci sinif.
 * <p>
 * Bu sinifin varligi, Clean Architecture'in Bagimlilik Kuralini korumanin
 * pratik yoludur: domain katmani ({@code Patient}) hicbir JPA anotasyonu
 * icermez; JPA'ya ozel alanlar/anotasyonlar SADECE {@code PatientEntity}'de
 * bulunur. Iki dunya arasindaki tek baglanti noktasi bu mapper'dir.
 * {@code Patient}'in cross-aggregate (baska bir entity'ye referans) alani
 * olmadigindan, bu mapper disaridan ekstra bir bagimlilik almadan
 * kendi basina tam donusum yapabilir.
 */
public final class PatientMapper {

    private PatientMapper() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static PatientEntity toEntity(Patient hasta) {
        PatientEntity entity = new PatientEntity();
        entity.setId(hasta.getId());
        entity.setAd(hasta.getAd());
        entity.setSoyad(hasta.getSoyad());
        entity.setTcKimlikNo(hasta.getTcKimlikNo());
        entity.setEmail(hasta.getEmail());
        entity.setTelefonNo(hasta.getTelefonNo());
        entity.setDogumTarihi(hasta.getDogumTarihi());
        entity.setKanGrubu(hasta.getKanGrubu());
        entity.setSigortaNo(hasta.getSigortaNo());
        entity.setAlerjiler(new ArrayList<>(hasta.getAlerjiler()));
        return entity;
    }

    public static Patient toDomain(PatientEntity entity) {
        Patient hasta = new Patient(entity.getId(), entity.getAd(), entity.getSoyad(), entity.getTcKimlikNo(),
                entity.getEmail(), entity.getTelefonNo(), entity.getDogumTarihi(),
                entity.getKanGrubu(), entity.getSigortaNo());
        for (String alerji : entity.getAlerjiler()) {
            hasta.alerjiEkle(alerji);
        }
        return hasta;
    }
}
