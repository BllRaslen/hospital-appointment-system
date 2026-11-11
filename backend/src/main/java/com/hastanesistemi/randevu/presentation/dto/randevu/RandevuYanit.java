package com.hastanesistemi.randevu.presentation.dto.randevu;

import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.appointment.AppointmentStatus;
import com.hastanesistemi.randevu.presentation.dto.doktor.DoktorYanit;
import com.hastanesistemi.randevu.presentation.dto.hasta.HastaYanit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * {@code Appointment} domain nesnesinin API gorunumu. {@code efektifUcret},
 * {@code RandevuServisi.efektifUcretHesapla} (Strategy deseni) ile hesaplanan
 * degerdir - bu yuzden Appointment'in kendisinden degil, controller'in
 * ayrica sagladigi degerden olusturulur.
 */
public record RandevuYanit(Long id, HastaYanit hasta, DoktorYanit doktor, String hizmetAdi, String tesisAdi,
                            LocalDateTime baslangic, LocalDateTime bitis, AppointmentStatus durum,
                            BigDecimal efektifUcret) {

    public static RandevuYanit fromDomain(Appointment randevu, BigDecimal efektifUcret) {
        return new RandevuYanit(randevu.getId(), HastaYanit.fromDomain(randevu.getHasta()),
                DoktorYanit.fromDomain(randevu.getDoktor()), randevu.getHizmet().getAd(),
                randevu.getTesis().getAd(), randevu.getZamanAraligi().getBaslangic(),
                randevu.getZamanAraligi().getBitis(), randevu.getDurum(), efektifUcret);
    }
}
