package com.hastanesistemi.randevu.presentation.dto.tesis;

import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;

/**
 * {@code HealthcareFacility} (Hospital/Clinic) domain nesnesinin API
 * gorunumu. {@code tesisTuru} alani, COK BICIMLI {@code getTesisTuru()}
 * metodunun sonucunu tasir ("HASTANE"/"KLINIK") - frontend, hangi somut
 * Java sinifiyla (Hospital/Clinic) calistigini hic bilmek zorunda degildir.
 */
public record TesisYanit(Long id, String ad, String tesisTuru, String sehir, int gunlukMaksimumRandevuKapasitesi) {

    public static TesisYanit fromDomain(HealthcareFacility tesis) {
        String sehir = tesis.getAdres() != null ? tesis.getAdres().getSehir() : null;
        return new TesisYanit(tesis.getId(), tesis.getAd(), tesis.getTesisTuru(), sehir,
                tesis.getGunlukMaksimumRandevuKapasitesi());
    }
}
