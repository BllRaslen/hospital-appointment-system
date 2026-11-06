package com.hastanesistemi.randevu.domain.model.person;

import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Hastane/Klinik yoneticisini temsil eder. Gereksinim: "Hastane / Klinik
 * Yoneticisi" rolu.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - aylikEkOdemeHesapla(), yonetici icin maasin
 * yuzdesi olarak farkli bir formulle hesaplanir (Doctor ve Staff'tan farkli).
 */
public class FacilityManager extends Employee {

    private static final BigDecimal YONETICI_BONUS_ORANI = BigDecimal.valueOf(0.10);

    private HealthcareFacility yonettigiTesis;

    public FacilityManager(Long id, String ad, String soyad, String tcKimlikNo, String email,
                            String telefonNo, LocalDate dogumTarihi, String calisanNo,
                            LocalDate iseBaslamaTarihi, BigDecimal temelMaas) {
        super(id, ad, soyad, tcKimlikNo, email, telefonNo, dogumTarihi, calisanNo,
                iseBaslamaTarihi, temelMaas);
    }

    @Override
    public Role getRol() {
        return Role.YONETICI;
    }

    @Override
    public String getYetkiAciklamasi() {
        return "Tesis bilgilerini, calisanlari ve hizmetleri yonetir; randevu politikalarini belirler.";
    }

    @Override
    public BigDecimal aylikEkOdemeHesapla() {
        return temelMaas.multiply(YONETICI_BONUS_ORANI);
    }

    public HealthcareFacility getYonettigiTesis() {
        return yonettigiTesis;
    }

    public void setYonettigiTesis(HealthcareFacility yonettigiTesis) {
        this.yonettigiTesis = yonettigiTesis;
    }
}
