package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.port.out.HealthcareFacilityRepository;
import com.hastanesistemi.randevu.presentation.dto.tesis.TesisYanit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Hastane/klinik tesislerini listeleyen salt-okunur endpoint. Randevu
 * olusturma akisinda hastanin hangi tesiste randevu almak istedigini
 * secebilmesi icin gereklidir.
 */
@RestController
@RequestMapping("/api/tesisler")
public class TesisController {

    private final HealthcareFacilityRepository healthcareFacilityRepository;

    public TesisController(HealthcareFacilityRepository healthcareFacilityRepository) {
        this.healthcareFacilityRepository = healthcareFacilityRepository;
    }

    @GetMapping
    public List<TesisYanit> hepsiniGetir() {
        return healthcareFacilityRepository.hepsiniGetir().stream().map(TesisYanit::fromDomain).toList();
    }
}
