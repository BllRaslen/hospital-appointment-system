package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.port.out.MedicalServiceRepository;
import com.hastanesistemi.randevu.presentation.dto.hizmet.HizmetYanit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kurumun sundugu saglik hizmetlerini listeleyen salt-okunur endpoint.
 * Randevu olusturma akisinda hastanin hangi hizmeti talep ettigini
 * secebilmesi icin gereklidir.
 */
@RestController
@RequestMapping("/api/hizmetler")
public class HizmetController {

    private final MedicalServiceRepository medicalServiceRepository;

    public HizmetController(MedicalServiceRepository medicalServiceRepository) {
        this.medicalServiceRepository = medicalServiceRepository;
    }

    @GetMapping
    public List<HizmetYanit> hepsiniGetir() {
        return medicalServiceRepository.hepsiniGetir().stream().map(HizmetYanit::fromDomain).toList();
    }
}
