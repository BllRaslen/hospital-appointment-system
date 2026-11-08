package com.hastanesistemi.randevu.infrastructure.persistence.adapter;

import com.hastanesistemi.randevu.application.port.out.MedicalServiceRepository;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.MedicalServiceMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.MedicalServiceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@code MedicalServiceRepository} portunun JPA ile gerceklestirilmesi.
 * Bkz. {@link PatientRepositoryAdapter} ic dokumantasyonu (Adapter deseni).
 */
@Repository
public class MedicalServiceRepositoryAdapter implements MedicalServiceRepository {

    private final MedicalServiceJpaRepository medicalServiceJpaRepository;

    public MedicalServiceRepositoryAdapter(MedicalServiceJpaRepository medicalServiceJpaRepository) {
        this.medicalServiceJpaRepository = medicalServiceJpaRepository;
    }

    @Override
    public MedicalService kaydet(MedicalService hizmet) {
        MedicalServiceEntity kaydedilen = medicalServiceJpaRepository.save(MedicalServiceMapper.toEntity(hizmet));
        return MedicalServiceMapper.toDomain(kaydedilen);
    }

    @Override
    public Optional<MedicalService> idIleBul(Long id) {
        return medicalServiceJpaRepository.findById(id).map(MedicalServiceMapper::toDomain);
    }

    @Override
    public List<MedicalService> hepsiniGetir() {
        return medicalServiceJpaRepository.findAll().stream().map(MedicalServiceMapper::toDomain).toList();
    }
}
