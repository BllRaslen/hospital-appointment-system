package com.hastanesistemi.randevu.domain.model.person;

import com.hastanesistemi.randevu.domain.model.facility.Department;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Kurumda calisan tum personeli (Doktor, Saglik Personeli, Yonetici) temsil
 * eden SOYUT (abstract) ara sinif.
 * <p>
 * OOP ilkesi: KALITIM (coklu seviyeli - multilevel inheritance) -
 * Person -&gt; Employee -&gt; (Doctor | Staff | FacilityManager).
 * Employee, Person'dan kisisel bilgileri miras alir ve bunlara calisana ozgu
 * alanlar (calisanNo, temelMaas, departman) ekler. Boylece her calisan turu
 * hem "kisi" hem de "calisan" davranislarini tasir.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - aylikEkOdemeHesapla() SOYUT bir metottur;
 * her calisan turu ek odemesini kendi kuraliyla hesaplar (Doktor: tamamlanan
 * randevu sayisina gore, Personel: sabit bonus, Yonetici: maas yuzdesine
 * gore). toplamAylikOdemeHesapla() ise ORTAK (concrete) bir metot olup,
 * SABLON METOT (template method) deseninin basit bir ornegidir: sabit kismi
 * (temelMaas + ek odeme) burada tanimlanir, degisken kismi (ek odemenin NASIL
 * hesaplandigi) alt sinifa birakilir.
 */
public abstract class Employee extends Person {

    protected final String calisanNo;
    protected final LocalDate iseBaslamaTarihi;
    protected BigDecimal temelMaas;
    private Department departman;

    protected Employee(Long id, String ad, String soyad, String tcKimlikNo, String email,
                        String telefonNo, LocalDate dogumTarihi, String calisanNo,
                        LocalDate iseBaslamaTarihi, BigDecimal temelMaas) {
        super(id, ad, soyad, tcKimlikNo, email, telefonNo, dogumTarihi);
        if (calisanNo == null || calisanNo.isBlank()) {
            throw new IllegalArgumentException("Calisan numarasi bos olamaz.");
        }
        if (temelMaas == null || temelMaas.signum() < 0) {
            throw new IllegalArgumentException("Temel maas negatif olamaz.");
        }
        this.calisanNo = calisanNo;
        this.iseBaslamaTarihi = iseBaslamaTarihi;
        this.temelMaas = temelMaas;
    }

    /**
     * Calisan turune ozgu ek odeme (bonus/prim) hesaplama kurali.
     * OOP ilkesi: COK BICIMLILIK - her alt sinifta farkli uygulanir.
     */
    public abstract BigDecimal aylikEkOdemeHesapla();

    /**
     * SABLON METOT (template method): degismeyen hesaplama iskeleti burada,
     * degisken parca (aylikEkOdemeHesapla) alt sinifa delege edilir.
     */
    public final BigDecimal toplamAylikOdemeHesapla() {
        return temelMaas.add(aylikEkOdemeHesapla());
    }

    public String getCalisanNo() {
        return calisanNo;
    }

    public LocalDate getIseBaslamaTarihi() {
        return iseBaslamaTarihi;
    }

    public BigDecimal getTemelMaas() {
        return temelMaas;
    }

    public void setTemelMaas(BigDecimal temelMaas) {
        if (temelMaas == null || temelMaas.signum() < 0) {
            throw new IllegalArgumentException("Temel maas negatif olamaz.");
        }
        this.temelMaas = temelMaas;
    }

    public Department getDepartman() {
        return departman;
    }

    public void setDepartman(Department departman) {
        this.departman = departman;
    }
}
