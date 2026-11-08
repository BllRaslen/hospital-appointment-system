package com.hastanesistemi.randevu.infrastructure.persistence.adapter;

import com.hastanesistemi.randevu.application.port.out.DoctorRepository;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.DoctorMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.DoctorJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.MedicalServiceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@code DoctorRepository} portunun JPA ile gerceklestirilmesi. Bkz.
 * {@link PatientRepositoryAdapter} ic dokumantasyonu (Adapter deseni).
 * <p>
 * {@code sunduguHizmetler} GIBI CROSS-AGGREGATE bir iliskiyi kaydederken,
 * doktorun hizmet listesindeki her {@code MedicalService}'in ZATEN VAR OLAN
 * veritabani kaydina {@code getReferenceById} ile (ekstra SELECT sorgusu
 * olmadan) referans verilir — mapper'in KENDI BASINA yeni bir hizmet satiri
 * olusturmaya calismasi ONLENMIS olur (bkz. {@code DoctorMapper} ic
 * dokumantasyonu).
 */
@Repository
public class DoctorRepositoryAdapter implements DoctorRepository {

    private final DoctorJpaRepository doctorJpaRepository;
    private final MedicalServiceJpaRepository medicalServiceJpaRepository;

    public DoctorRepositoryAdapter(DoctorJpaRepository doctorJpaRepository,
                                    MedicalServiceJpaRepository medicalServiceJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.medicalServiceJpaRepository = medicalServiceJpaRepository;
    }

    @Override
    public Doctor kaydet(Doctor doktor) {
        List<MedicalServiceEntity> hizmetReferanslari = doktor.getSunduguHizmetler().stream()
                .map(this::hizmetReferansi)
                .toList();
        DoctorEntity kaydedilen = doctorJpaRepository.save(DoctorMapper.toEntity(doktor, hizmetReferanslari));
        return DoctorMapper.toDomain(kaydedilen);
    }

    private MedicalServiceEntity hizmetReferansi(MedicalService hizmet) {
        return medicalServiceJpaRepository.getReferenceById(hizmet.getId());
    }

    @Override
    public Optional<Doctor> idIleBul(Long id) {
        return doctorJpaRepository.findById(id).map(DoctorMapper::toDomain);
    }

    @Override
    public List<Doctor> uzmanlikAlaninaGoreBul(Specialty uzmanlikAlani) {
        return doctorJpaRepository.findByUzmanlikAlani(uzmanlikAlani.name()).stream()
                .map(DoctorMapper::toDomain)
                .toList();
    }

    @Override
    public List<Doctor> hepsiniGetir() {
        return doctorJpaRepository.findAll().stream().map(DoctorMapper::toDomain).toList();
    }
}
