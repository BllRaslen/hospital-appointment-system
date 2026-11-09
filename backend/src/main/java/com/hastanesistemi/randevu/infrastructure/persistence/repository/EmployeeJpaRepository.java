package com.hastanesistemi.randevu.infrastructure.persistence.repository;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA teknik detayi. Bkz. {@link PatientJpaRepository} ic
 * dokumantasyonu (adapter/port ayrimi).
 * <p>
 * {@code DoctorJpaRepository}'den FARKLI olarak, bu arayuz is kurallari icin
 * degil, SADECE kimlik dogrulama (login) icin vardir: {@code EmployeeEntity}
 * {@code calisanlar} tablosunun KOKUDUR (SINGLE_TABLE kalitim), bu yuzden
 * {@code findByEmail} calisan turu (Doktor/SaglikPersoneli/Yonetici) HERHANGI
 * biri olsun, TEK sorguyla polimorfik olarak sonuc doner — Hibernate,
 * discriminator kolonuna gore dogru somut alt sinifin (DoctorEntity/
 * StaffEntity/FacilityManagerEntity) ornegini olusturur. Bkz.
 * {@code infrastructure.security.KullaniciDetayServisi}.
 */
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String email);
}
