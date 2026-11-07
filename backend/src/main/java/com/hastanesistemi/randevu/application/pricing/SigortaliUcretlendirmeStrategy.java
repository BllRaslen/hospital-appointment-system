package com.hastanesistemi.randevu.application.pricing;

import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Sigorta numarasi tanimli (sigortali) hastalara sabit bir oranda indirim
 * uygulayan strateji; sigortasi olmayan hastalar icin tam ucret gecerlidir.
 * Bkz. {@link UcretlendirmeStrategy}.
 */
public class SigortaliUcretlendirmeStrategy implements UcretlendirmeStrategy {

    private static final BigDecimal SIGORTA_INDIRIM_ORANI = BigDecimal.valueOf(0.20);

    @Override
    public BigDecimal hesapla(MedicalService hizmet, Patient hasta) {
        BigDecimal tamUcret = hizmet.getUcret();
        boolean sigortali = hasta.getSigortaNo() != null && !hasta.getSigortaNo().isBlank();
        if (!sigortali) {
            return tamUcret;
        }
        BigDecimal indirimTutari = tamUcret.multiply(SIGORTA_INDIRIM_ORANI);
        return tamUcret.subtract(indirimTutari).setScale(2, RoundingMode.HALF_UP);
    }
}
