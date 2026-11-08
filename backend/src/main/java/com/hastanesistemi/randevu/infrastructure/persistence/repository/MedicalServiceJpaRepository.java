package com.hastanesistemi.randevu.infrastructure.persistence.repository;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA teknik detayi. Bkz. {@link PatientJpaRepository} ic
 * dokumantasyonu (adapter/port ayrimi).
 */
public interface MedicalServiceJpaRepository extends JpaRepository<MedicalServiceEntity, Long> {
}
