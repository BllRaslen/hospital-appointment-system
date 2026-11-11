package com.hastanesistemi.randevu.presentation.dto.hizmet;

import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.math.BigDecimal;

/** {@code MedicalService} domain nesnesinin API gorunumu. Bkz. {@code HastaYanit}. */
public record HizmetYanit(Long id, String ad, String aciklama, int sureDakika, BigDecimal ucret) {

    public static HizmetYanit fromDomain(MedicalService hizmet) {
        return new HizmetYanit(hizmet.getId(), hizmet.getAd(), hizmet.getAciklama(),
                hizmet.getSureDakika(), hizmet.getUcret());
    }
}
