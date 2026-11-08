package com.hastanesistemi.randevu.infrastructure.persistence.adapter;

import com.hastanesistemi.randevu.application.port.out.AppointmentRepository;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.AppointmentEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.AppointmentMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.AppointmentJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.DoctorJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.HealthcareFacilityJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.MedicalServiceJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.PatientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@code AppointmentRepository} portunun JPA ile gerceklestirilmesi. Bkz.
 * {@link com.hastanesistemi.randevu.infrastructure.persistence.adapter.PatientRepositoryAdapter}
 * ic dokumantasyonu (Adapter deseni).
 * <p>
 * Bu adapter, {@code RandevuServisi}'nin (application/service) kurucusuna
 * enjekte edilen {@code AppointmentRepository} PORTUNUN GERCEK (runtime)
 * implementasyonudur — Spring, uygulama baglami (context) ayaga kalkarken
 * bu sinifi otomatik olarak bulur ve enjekte eder. {@code RandevuServisi}
 * kodunda BU SINIFIN ADI HICBIR YERDE GECMEZ; bu, Dependency Inversion'in
 * (bagimliligin somut degil soyut tipe yapilmasi) en net kanitidir.
 */
@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final MedicalServiceJpaRepository medicalServiceJpaRepository;
    private final HealthcareFacilityJpaRepository healthcareFacilityJpaRepository;

    public AppointmentRepositoryAdapter(AppointmentJpaRepository appointmentJpaRepository,
                                         PatientJpaRepository patientJpaRepository,
                                         DoctorJpaRepository doctorJpaRepository,
                                         MedicalServiceJpaRepository medicalServiceJpaRepository,
                                         HealthcareFacilityJpaRepository healthcareFacilityJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.medicalServiceJpaRepository = medicalServiceJpaRepository;
        this.healthcareFacilityJpaRepository = healthcareFacilityJpaRepository;
    }

    @Override
    public Appointment kaydet(Appointment randevu) {
        PatientEntity hastaReferansi = patientJpaRepository.getReferenceById(randevu.getHasta().getId());
        DoctorEntity doktorReferansi = doctorJpaRepository.getReferenceById(randevu.getDoktor().getId());
        MedicalServiceEntity hizmetReferansi = medicalServiceJpaRepository.getReferenceById(randevu.getHizmet().getId());
        HealthcareFacilityEntity tesisReferansi = healthcareFacilityJpaRepository.getReferenceById(randevu.getTesis().getId());

        AppointmentEntity kaydedilen = appointmentJpaRepository.save(
                AppointmentMapper.toEntity(randevu, hastaReferansi, doktorReferansi, hizmetReferansi, tesisReferansi));
        return AppointmentMapper.toDomain(kaydedilen);
    }

    @Override
    public Optional<Appointment> idIleBul(Long id) {
        return appointmentJpaRepository.findById(id).map(AppointmentMapper::toDomain);
    }

    @Override
    public List<Appointment> doktorunRandevulariniGetir(Doctor doktor) {
        DoctorEntity doktorReferansi = doctorJpaRepository.getReferenceById(doktor.getId());
        return appointmentJpaRepository.findByDoktor(doktorReferansi).stream()
                .map(AppointmentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Appointment> hastaninRandevulariniGetir(Patient hasta) {
        PatientEntity hastaReferansi = patientJpaRepository.getReferenceById(hasta.getId());
        return appointmentJpaRepository.findByHasta(hastaReferansi).stream()
                .map(AppointmentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Appointment> hepsiniGetir() {
        return appointmentJpaRepository.findAll().stream().map(AppointmentMapper::toDomain).toList();
    }
}
