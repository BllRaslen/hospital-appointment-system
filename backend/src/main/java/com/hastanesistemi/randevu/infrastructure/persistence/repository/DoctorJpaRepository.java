package com.hastanesistemi.randevu.infrastructure.persistence.repository;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA teknik detayi. Bkz. {@link PatientJpaRepository} ic
 * dokumantasyonu (adapter/port ayrimi).
 */
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Long> {

    List<DoctorEntity> findByUzmanlikAlani(String uzmanlikAlani);
}
