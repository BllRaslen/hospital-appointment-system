package com.hastanesistemi.randevu.infrastructure.persistence.adapter;

import com.hastanesistemi.randevu.application.port.out.HealthcareFacilityRepository;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.HealthcareFacilityMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.HealthcareFacilityJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.MedicalServiceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@code HealthcareFacilityRepository} portunun JPA ile gerceklestirilmesi.
 * Bkz. {@link PatientRepositoryAdapter} ve {@link DoctorRepositoryAdapter}
 * ic dokumantasyonlari (Adapter deseni + cross-aggregate referans cozumleme).
 */
@Repository
public class HealthcareFacilityRepositoryAdapter implements HealthcareFacilityRepository {

    private final HealthcareFacilityJpaRepository healthcareFacilityJpaRepository;
    private final MedicalServiceJpaRepository medicalServiceJpaRepository;

    public HealthcareFacilityRepositoryAdapter(HealthcareFacilityJpaRepository healthcareFacilityJpaRepository,
                                                MedicalServiceJpaRepository medicalServiceJpaRepository) {
        this.healthcareFacilityJpaRepository = healthcareFacilityJpaRepository;
        this.medicalServiceJpaRepository = medicalServiceJpaRepository;
    }

    @Override
    public HealthcareFacility kaydet(HealthcareFacility tesis) {
        List<MedicalServiceEntity> hizmetReferanslari = tesis.getSunduguHizmetler().stream()
                .map(this::hizmetReferansi)
                .toList();
        HealthcareFacilityEntity kaydedilen = healthcareFacilityJpaRepository.save(
                HealthcareFacilityMapper.toEntity(tesis, hizmetReferanslari));
        return HealthcareFacilityMapper.toDomain(kaydedilen);
    }

    private MedicalServiceEntity hizmetReferansi(MedicalService hizmet) {
        return medicalServiceJpaRepository.getReferenceById(hizmet.getId());
    }

    @Override
    public Optional<HealthcareFacility> idIleBul(Long id) {
        return healthcareFacilityJpaRepository.findById(id).map(HealthcareFacilityMapper::toDomain);
    }

    @Override
    public List<HealthcareFacility> hepsiniGetir() {
        return healthcareFacilityJpaRepository.findAll().stream().map(HealthcareFacilityMapper::toDomain).toList();
    }
}
