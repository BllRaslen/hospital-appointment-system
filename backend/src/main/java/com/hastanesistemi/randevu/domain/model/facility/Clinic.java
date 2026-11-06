package com.hastanesistemi.randevu.domain.model.facility;

import com.hastanesistemi.randevu.domain.model.common.Address;

/**
 * Klinigi temsil eder (genellikle daha kucuk olcekli, muayene odasi sayisiyla
 * sinirli kapasiteye sahip tesis).
 * <p>
 * OOP ilkesi: KALITIM / COK BICIMLILIK - Hospital'dan TAMAMEN FARKLI bir
 * kapasite formulu kullanir; bu, aynı soyut sozlesmenin (HealthcareFacility)
 * farkli somut siniflarca nasil farkli yorumlanabildigini gosterir.
 */
public class Clinic extends HealthcareFacility {

    private int muayeneOdasiSayisi;

    public Clinic(Long id, String ad, Address adres, String vergiNo, int muayeneOdasiSayisi) {
        super(id, ad, adres, vergiNo);
        this.muayeneOdasiSayisi = muayeneOdasiSayisi;
    }

    @Override
    public String getTesisTuru() {
        return "KLINIK";
    }

    @Override
    public int getGunlukMaksimumRandevuKapasitesi() {
        return Math.max(muayeneOdasiSayisi, 1) * 15;
    }

    public int getMuayeneOdasiSayisi() {
        return muayeneOdasiSayisi;
    }

    public void setMuayeneOdasiSayisi(int muayeneOdasiSayisi) {
        this.muayeneOdasiSayisi = muayeneOdasiSayisi;
    }
}
