package com.hastanesistemi.randevu.infrastructure.persistence.adapter;

import com.hastanesistemi.randevu.application.port.out.PatientRepository;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.PatientMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.PatientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@code PatientRepository} PORTUNUN (application katmani) PostgreSQL/JPA
 * uzerinden gerceklestirilmesi (implementasyonu).
 * <p>
 * OOP ilkesi: ADAPTER DESENI - bu sinif, Spring Data'nin saglayici-ozel
 * ({@code PatientJpaRepository}) API'sini, application katmaninin beklediği
 * port arayuzune ({@code PatientRepository}) UYARLAR (adapt). RandevuServisi
 * gibi use-case servisleri bu sinifin VARLIGINDAN BILE HABERDAR DEGILDIR;
 * sadece {@code PatientRepository} arayuzunu bilirler — Spring, bu somut
 * implementasyonu calisma zamaninda (runtime) enjekte eder (Dependency
 * Injection + Dependency Inversion).
 */
@Repository
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    public PatientRepositoryAdapter(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public Patient kaydet(Patient hasta) {
        PatientEntity kaydedilen = patientJpaRepository.save(PatientMapper.toEntity(hasta));
        return PatientMapper.toDomain(kaydedilen);
    }

    @Override
    public Optional<Patient> idIleBul(Long id) {
        return patientJpaRepository.findById(id).map(PatientMapper::toDomain);
    }

    @Override
    public Optional<Patient> tcKimlikNoIleBul(String tcKimlikNo) {
        return patientJpaRepository.findByTcKimlikNo(tcKimlikNo).map(PatientMapper::toDomain);
    }

    @Override
    public List<Patient> hepsiniGetir() {
        return patientJpaRepository.findAll().stream().map(PatientMapper::toDomain).toList();
    }
}
