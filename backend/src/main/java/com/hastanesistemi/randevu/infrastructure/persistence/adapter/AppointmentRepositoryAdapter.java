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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * {@code AppointmentRepository} portunun JPA ile gerceklestirilmesi. Bkz.
 * {@link com.hastanesistemi.randevu.infrastructure.persistence.adapter.PatientRepositoryAdapter}
 * ic dokumantasyonu (Adapter deseni + sinif seviyesindeki {@code
 * @Transactional}'in nedeni - {@code AppointmentMapper.toDomain}, hasta/
 * doktor/tesis icin {@code PatientMapper}/{@code DoctorMapper}/{@code
 * HealthcareFacilityMapper}'i COGRAZTIRIR; bunlarin HER BIRI kendi LAZY
 * koleksiyonlarina erisir).
 * <p>
 * Bu adapter, {@code RandevuServisi}'nin (application/service) kurucusuna
 * enjekte edilen {@code AppointmentRepository} PORTUNUN GERCEK (runtime)
 * implementasyonudur — Spring, uygulama baglami (context) ayaga kalkarken
 * bu sinifi otomatik olarak bulur ve enjekte eder. {@code RandevuServisi}
 * kodunda BU SINIFIN ADI HICBIR YERDE GECMEZ; bu, Dependency Inversion'in
 * (bagimliligin somut degil soyut tipe yapilmasi) en net kanitidir.
 */
@Repository
@Transactional
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
        // NOT: tesis icin DAHA sonra getReferenceById KULLANILAMAZ. Aksinin
        // tersine Doctor/MedicalService/Patient icin, HealthcareFacilityJpaRepository
        // SOYUT KOK tipe (HealthcareFacilityEntity) gore tanimlidir (Hospital/Clinic
        // AYNI SINGLE_TABLE'i paylasir); bu yuzden getReferenceById, GERCEK
        // alt turu (Hospital/Clinic) BILMEDEN bir proxy doner - HealthcareFacilityMapper'in
        // asagida (toDomain cagrisinda) yaptigi "instanceof HospitalEntity/ClinicEntity"
        // kontrolu, proxy SATIRA ERISILMEDEN (bu metot zaten burada herhangi
        // bir alan OKUMADIGI icin) HICBIR ZAMAN dogru sonuc vermez. findById,
        // alt turu belirlemek icin zaten bir SELECT yapmak ZORUNDA oldugundan
        // (discriminator kolonu okunmadan dogru Java sinifi seciLEMEZ), burada
        // "ekstra sorgu olmadan referans al" optimizasyonu tesis icin pratikte
        // UYGULANAMAZ.
        HealthcareFacilityEntity tesisReferansi = healthcareFacilityJpaRepository.findById(randevu.getTesis().getId())
                .orElseThrow(() -> new IllegalArgumentException("Tesis bulunamadi: id=" + randevu.getTesis().getId()));

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
