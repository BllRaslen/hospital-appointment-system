package com.hastanesistemi.randevu.application.port.out;

import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.util.List;
import java.util.Optional;

/**
 * Saglik hizmeti (MedicalService) tanimlarinin kalici hale getirilmesi/
 * sorgulanmasi icin port. Bkz. {@link PatientRepository} ic dokumantasyonu
 * (Repository deseni).
 */
public interface MedicalServiceRepository {

    MedicalService kaydet(MedicalService hizmet);

    Optional<MedicalService> idIleBul(Long id);

    List<MedicalService> hepsiniGetir();
}
