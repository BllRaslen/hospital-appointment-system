package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.port.out.PatientRepository;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.infrastructure.security.RandevuKullanicisi;
import com.hastanesistemi.randevu.presentation.dto.hasta.HastaYanit;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Oturum acmis hastanin KENDI profilini goruntulemesini saglayan endpoint.
 * Hasta id'si yol parametresi DEGIL, JWT'den cozumlenen {@code
 * RandevuKullanicisi} principal'indan alinir; boylece bir hasta BASKA bir
 * hastanin profiline erisemez.
 */
@RestController
@RequestMapping("/api/hastalar")
public class HastaController {

    private final PatientRepository patientRepository;

    public HastaController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/ben")
    @PreAuthorize("hasRole('HASTA')")
    public HastaYanit kendiProfiliniGetir(@AuthenticationPrincipal RandevuKullanicisi kullanici) {
        Patient hasta = patientRepository.idIleBul(kullanici.getKullaniciId())
                .orElseThrow(() -> new IllegalArgumentException("Hasta bulunamadi: id=" + kullanici.getKullaniciId()));
        return HastaYanit.fromDomain(hasta);
    }
}
