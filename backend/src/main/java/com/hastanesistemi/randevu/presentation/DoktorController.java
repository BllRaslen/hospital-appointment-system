package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.port.out.DoctorRepository;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.presentation.dto.doktor.DoktorYanit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Doktor listeleme endpoint'leri. Hastalarin randevu olustururken uygun
 * doktoru/uzmanlik alanini secebilmesi icin gereklidir. Salt-okunur
 * oldugundan ve ek is kurali (business rule) icermediginden, use-case
 * servisine ihtiyac yoktur - controller DOGRUDAN {@code DoctorRepository}
 * PORTUNU kullanir.
 */
@RestController
@RequestMapping("/api/doktorlar")
public class DoktorController {

    private final DoctorRepository doctorRepository;

    public DoktorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public List<DoktorYanit> hepsiniGetir() {
        return doctorRepository.hepsiniGetir().stream().map(DoktorYanit::fromDomain).toList();
    }

    @GetMapping("/uzmanlik/{uzmanlikAlani}")
    public List<DoktorYanit> uzmanlikAlaninaGoreGetir(@PathVariable Specialty uzmanlikAlani) {
        return doctorRepository.uzmanlikAlaninaGoreBul(uzmanlikAlani).stream().map(DoktorYanit::fromDomain).toList();
    }
}
