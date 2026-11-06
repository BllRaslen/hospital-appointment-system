package com.hastanesistemi.randevu.domain.model.person;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Doktor disindaki saglik personelini (hemsire, teknisyen vb.) temsil eder.
 * Gereksinim: "Doktor ve saglik personeli sisteme kayit edilebilecek."
 * <p>
 * OOP ilkesi: COK BICIMLILIK - aylikEkOdemeHesapla() burada Doctor'dan
 * TAMAMEN FARKLI bir kuralla (sabit bonus) uygulanir; bu, aynı soyut metodun
 * farkli alt siniflarda farkli davranabilecegini gosteren klasik bir ornektir.
 */
public class Staff extends Employee {

    private static final BigDecimal SABIT_AYLIK_BONUS = BigDecimal.valueOf(500);

    private String unvan;

    public Staff(Long id, String ad, String soyad, String tcKimlikNo, String email,
                 String telefonNo, LocalDate dogumTarihi, String calisanNo,
                 LocalDate iseBaslamaTarihi, BigDecimal temelMaas, String unvan) {
        super(id, ad, soyad, tcKimlikNo, email, telefonNo, dogumTarihi, calisanNo,
                iseBaslamaTarihi, temelMaas);
        this.unvan = unvan;
    }

    @Override
    public Role getRol() {
        return Role.SAGLIK_PERSONELI;
    }

    @Override
    public String getYetkiAciklamasi() {
        return "Doktorlara destek olur; hasta kabul ve on hazirlik islemlerini yurutur.";
    }

    @Override
    public BigDecimal aylikEkOdemeHesapla() {
        return SABIT_AYLIK_BONUS;
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }
}
