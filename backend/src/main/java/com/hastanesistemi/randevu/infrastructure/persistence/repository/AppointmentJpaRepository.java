package com.hastanesistemi.randevu.infrastructure.persistence.repository;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.AppointmentEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA teknik detayi. Bkz. {@link PatientJpaRepository} ic
 * dokumantasyonu (adapter/port ayrimi).
 */
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByDoktor(DoctorEntity doktor);

    List<AppointmentEntity> findByHasta(PatientEntity hasta);
}
