package com.hastanesistemi.randevu.domain.model.person;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Sistemdeki tum gercek kisileri (Hasta, Doktor, Saglik Personeli, Yonetici)
 * temsil eden SOYUT (abstract) temel sinif.
 * <p>
 * OOP ilkesi: SOYUTLAMA (abstraction) + KALITIM (inheritance) - Person,
 * tek basina ornegi (instance) olusturulamayan soyut bir siniftir ("new
 * Person(...)" YAZILAMAZ). Sadece somut alt siniflar (Patient, Employee'nin
 * alt siniflari) uzerinden ornek olusturulabilir. Tum kisilerde ortak olan
 * alanlar (ad, soyad, tc kimlik no, iletisim bilgisi) ve davranislar
 * (getTamAd) burada BIR KEZ tanimlanir, alt siniflar bunu TEKRAR YAZMADAN
 * miras alir (kod tekrarini onler - DRY).
 * <p>
 * OOP ilkesi: KAPSULLEME (encapsulation) - Tum alanlar 'private'dir. Kimlik
 * bilgisi (ad, soyad, tcKimlikNo, dogumTarihi) nesne olusturulduktan sonra
 * DEGISTIRILEMEZ (setter yok); sadece iletisim bilgileri (email, telefonNo)
 * dogrulama (validation) icerecek kontrollu setter'lar ile guncellenebilir.
 * <p>
 * OOP ilkesi: COK BICIMLILIK (polymorphism) - getRol() ve getYetkiAciklamasi()
 * SOYUT (abstract) metotlardir; her alt sinif kendine ozgu bir uygulama
 * (implementation) saglar. Bir "List&lt;Person&gt;" uzerinde dongu kurup
 * person.getRol() cagirildiginda, calisma zamaninda (runtime) hangi alt
 * sinifin metodunun calisacagi otomatik olarak belirlenir (dinamik baglama -
 * dynamic dispatch).
 */
public abstract class Person {

    private final Long id;
    private final String ad;
    private final String soyad;
    private final String tcKimlikNo;
    private String email;
    private String telefonNo;
    private final LocalDate dogumTarihi;

    protected Person(Long id, String ad, String soyad, String tcKimlikNo,
                      String email, String telefonNo, LocalDate dogumTarihi) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Ad alani bos olamaz.");
        }
        if (soyad == null || soyad.isBlank()) {
            throw new IllegalArgumentException("Soyad alani bos olamaz.");
        }
        if (tcKimlikNo == null || !tcKimlikNo.matches("\\d{11}")) {
            throw new IllegalArgumentException("TC Kimlik No 11 haneli rakamlardan olusmalidir.");
        }
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlikNo = tcKimlikNo;
        this.dogumTarihi = dogumTarihi;
        setEmail(email);
        setTelefonNo(telefonNo);
    }

    /**
     * Her alt sinifin kendi rolunu dondurmesi gerekir.
     * OOP ilkesi: COK BICIMLILIK - ayni metot imzasi, farkli alt siniflarda
     * farkli sonuclar uretir (Patient -> HASTA, Doctor -> DOKTOR, vb.).
     */
    public abstract Role getRol();

    /**
     * Rolun sistemde neler yapabilecegini aciklayan insan-okunabilir metin.
     * OOP ilkesi: COK BICIMLILIK.
     */
    public abstract String getYetkiAciklamasi();

    /**
     * Tum alt siniflarin ORTAK olarak kullandigi, ekstra bir sey yazmasina
     * gerek olmayan davranis (kalitimin faydasi).
     */
    public String getTamAd() {
        return ad + " " + soyad;
    }

    public Long getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public LocalDate getDogumTarihi() {
        return dogumTarihi;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Kontrollu guncelleme: dogrulama kurali (basit e-posta formati) burada
     * merkezilestirilmistir. Dogrudan alan erisimi yerine bu metot kullanilir.
     */
    public void setEmail(String email) {
        if (email != null && !email.isBlank() && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Gecersiz e-posta formati: " + email);
        }
        this.email = email;
    }

    public String getTelefonNo() {
        return telefonNo;
    }

    public void setTelefonNo(String telefonNo) {
        if (telefonNo != null && !telefonNo.isBlank() && !telefonNo.matches("^[0-9+()\\s-]{7,20}$")) {
            throw new IllegalArgumentException("Gecersiz telefon numarasi formati: " + telefonNo);
        }
        this.telefonNo = telefonNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person that = (Person) o;
        return Objects.equals(tcKimlikNo, that.tcKimlikNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcKimlikNo);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ad='" + getTamAd() + "', rol=" + getRol() + "}";
    }
}
