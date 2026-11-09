package com.hastanesistemi.randevu.infrastructure.security;

import com.hastanesistemi.randevu.infrastructure.persistence.repository.EmployeeJpaRepository;
import com.hastanesistemi.randevu.infrastructure.persistence.repository.PatientJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security'nin kimlik dogrulama sirasinda cagirdigi
 * {@code UserDetailsService} sozlesmesinin uygulamasi.
 * <p>
 * Sistemde "tek bir kullanicilar tablosu" YOKTUR — hasta ve calisan (Doktor/
 * SaglikPersoneli/Yonetici) AYRI tablolarda tutulur (bkz. {@code PersonEntity}
 * hiyerarsisi). Bu servis, e-posta ile once hastalarda, bulamazsa calisanlarda
 * arama yaparak bu ayrimi kimlik dogrulama katmanindan GIZLER — cagiran kod
 * (Spring Security filtre zinciri) hangi tabloda bulundugunu hic bilmez,
 * sadece tek bir {@code UserDetails} sonucu gorur.
 */
@Service
public class KullaniciDetayServisi implements UserDetailsService {

    private final PatientJpaRepository patientJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;

    public KullaniciDetayServisi(PatientJpaRepository patientJpaRepository,
                                  EmployeeJpaRepository employeeJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
        this.employeeJpaRepository = employeeJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return patientJpaRepository.findByEmail(email)
                .map(RandevuKullanicisi::hastadanOlustur)
                .or(() -> employeeJpaRepository.findByEmail(email).map(RandevuKullanicisi::calisandanOlustur))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Bu e-posta ile kayitli kullanici bulunamadi: " + email));
    }
}
