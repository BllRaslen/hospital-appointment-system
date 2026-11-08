package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;

/**
 * {@code MedicalService} (domain) ile {@code MedicalServiceEntity} (JPA)
 * arasindaki donusumu yapan, durumsuz yardimci sinif. Bkz. {@link
 * PatientMapper} ic dokumantasyonu (mapper'in amaci).
 */
public final class MedicalServiceMapper {

    private MedicalServiceMapper() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static MedicalServiceEntity toEntity(MedicalService hizmet) {
        MedicalServiceEntity entity = new MedicalServiceEntity();
        entity.setId(hizmet.getId());
        entity.setAd(hizmet.getAd());
        entity.setAciklama(hizmet.getAciklama());
        entity.setSureDakika(hizmet.getSureDakika());
        entity.setUcret(hizmet.getUcret());
        return entity;
    }

    public static MedicalService toDomain(MedicalServiceEntity entity) {
        return new MedicalService(entity.getId(), entity.getAd(), entity.getAciklama(),
                entity.getSureDakika(), entity.getUcret());
    }
}
