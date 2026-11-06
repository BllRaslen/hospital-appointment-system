package com.hastanesistemi.randevu.domain.model.facility;

import com.hastanesistemi.randevu.domain.model.common.Address;

/**
 * Hastaneyi temsil eder (genellikle daha fazla bolum, yatak kapasitesi ve
 * acil servise sahip buyuk olcekli tesis).
 * <p>
 * OOP ilkesi: KALITIM / COK BICIMLILIK - HealthcareFacility'nin soyut
 * metotlarini KENDI KURALIYLA uygular.
 */
public class Hospital extends HealthcareFacility {

    private int yatakSayisi;
    private boolean acilServisVarMi;

    public Hospital(Long id, String ad, Address adres, String vergiNo,
                     int yatakSayisi, boolean acilServisVarMi) {
        super(id, ad, adres, vergiNo);
        this.yatakSayisi = yatakSayisi;
        this.acilServisVarMi = acilServisVarMi;
    }

    @Override
    public String getTesisTuru() {
        return "HASTANE";
    }

    /**
     * Hastaneler genelde daha fazla bolume sahip oldugundan, gunluk kapasite
     * bolum sayisina bagli olarak daha yuksek bir carpanla hesaplanir.
     */
    @Override
    public int getGunlukMaksimumRandevuKapasitesi() {
        return Math.max(bolumler.size(), 1) * 40;
    }

    public int getYatakSayisi() {
        return yatakSayisi;
    }

    public void setYatakSayisi(int yatakSayisi) {
        this.yatakSayisi = yatakSayisi;
    }

    public boolean isAcilServisVarMi() {
        return acilServisVarMi;
    }

    public void setAcilServisVarMi(boolean acilServisVarMi) {
        this.acilServisVarMi = acilServisVarMi;
    }
}
