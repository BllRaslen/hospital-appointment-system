package com.hastanesistemi.randevu.application.pricing;

import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.math.BigDecimal;

/**
 * Herhangi bir indirim uygulamayan, hizmetin tanimli ucretini oldugu gibi
 * donduren varsayilan strateji. Bkz. {@link UcretlendirmeStrategy}.
 */
public class StandartUcretlendirmeStrategy implements UcretlendirmeStrategy {

    @Override
    public BigDecimal hesapla(MedicalService hizmet, Patient hasta) {
        return hizmet.getUcret();
    }
}
