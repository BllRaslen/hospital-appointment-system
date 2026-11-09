package com.hastanesistemi.randevu.infrastructure.persistence.repository;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA'nin otomatik olarak implemente ettigi CRUD arayuzu.
 * <p>
 * Bu arayuz, application katmanindaki {@code PatientRepository} PORTU
 * DEGILDIR — Spring Data'ya ozel, infrastructure'a AIT teknik bir detaydir.
 * Application katmani bunu asla dogrudan GORMEZ; bunun yerine
 * {@code PatientRepositoryAdapter} bu arayuzu SARAR (wrap) ve application
 * portunu gerceklestirir. Bu ayrim, Clean Architecture'in Bagimlilik
 * Kuralini korur: application, Spring Data'ya degil, kendi portuna bagimlidir.
 */
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByTcKimlikNo(String tcKimlikNo);

    /**
     * Kimlik dogrulama (login) akisinda e-posta ile hasta aramak icin
     * kullanilir — bkz. {@code infrastructure.security.KullaniciDetayServisi}.
     */
    Optional<PatientEntity> findByEmail(String email);
}
