package com.hastanesistemi.randevu.infrastructure.security;

import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.DoctorMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.mapper.PatientMapper;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.DoctorJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.EmployeeJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.PatientJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Yeni kullanici (hasta/doktor) kaydi sirasinda sifre hash'lenmesini ve
 * e-posta benzersizligi kontrolunu KAPSULLEYEN infrastructure servisi.
 * <p>
 * BILINCLI OLARAK infrastructure katmanindadir, application katmaninda DEGIL:
 * sifre hash'leme ({@code PasswordEncoder}) ve e-posta benzersizligi kontrolu
 * (JPA repository sorgusu) birer ALTYAPI/GUVENLIK detayidir, is kurali DEGILDIR
 * (bkz. {@code PersonEntity} ic dokumantasyonu - sifreHash neden domain'de
 * degil de entity'de bulunur). Bu sinif, {@code KullaniciDetayServisi} ile
 * AYNI katmanda ve AYNI nedenle (kimlik dogrulama altyapisi) yasar.
 * <p>
 * {@code PatientRepository}/{@code DoctorRepository} PORTLARI yerine
 * DOGRUDAN JPA repository'leri kullanir, cunku application katmanindaki
 * portlarin {@code kaydet} metotlari sifre hash'i TASIYAMAZ (domain nesneleri
 * sifre bilgisi icermez) - bu metot, entity'yi mapper ile insa ettikten SONRA
 * sifre hash'ini ekleyip kaydeder.
 */
@Service
public class KayitServisi {

    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final PasswordEncoder sifreKodlayici;

    public KayitServisi(PatientJpaRepository patientJpaRepository,
                         DoctorJpaRepository doctorJpaRepository,
                         EmployeeJpaRepository employeeJpaRepository,
                         PasswordEncoder sifreKodlayici) {
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.employeeJpaRepository = employeeJpaRepository;
        this.sifreKodlayici = sifreKodlayici;
    }

    /** Hasta kendi kendine kayit (self sign-up) akisinda cagirilir. */
    public Patient hastaKaydet(Patient hasta, String duzMetinSifre) {
        epostaBenzersizliginiDogrula(hasta.getEmail());
        PatientEntity entity = PatientMapper.toEntity(hasta);
        entity.setSifreHash(sifreKodlayici.encode(duzMetinSifre));
        PatientEntity kaydedilen = patientJpaRepository.save(entity);
        return PatientMapper.toDomain(kaydedilen);
    }

    /** Bir yoneticinin yeni bir doktor hesabi acmasi akisinda cagirilir. */
    public Doctor doktorKaydet(Doctor doktor, String duzMetinSifre) {
        epostaBenzersizliginiDogrula(doktor.getEmail());
        DoctorEntity entity = DoctorMapper.toEntity(doktor, Collections.emptyList());
        entity.setSifreHash(sifreKodlayici.encode(duzMetinSifre));
        DoctorEntity kaydedilen = doctorJpaRepository.save(entity);
        return DoctorMapper.toDomain(kaydedilen);
    }

    /**
     * Hasta ve calisan tablolari AYRI oldugundan, benzersizlik HER IKI
     * tabloda da kontrol edilmelidir - aksi halde bir hasta ile bir doktor
     * ayni e-postayla kayit olabilir ve giris sirasinda hangisinin
     * kastedildigi belirsizlesir (bkz. {@code KullaniciDetayServisi}).
     */
    private void epostaBenzersizliginiDogrula(String email) {
        boolean hastaVar = patientJpaRepository.findByEmail(email).isPresent();
        boolean calisanVar = employeeJpaRepository.findByEmail(email).isPresent();
        if (hastaVar || calisanVar) {
            throw new IllegalArgumentException("Bu e-posta ile kayitli bir kullanici zaten var: " + email);
        }
    }
}
