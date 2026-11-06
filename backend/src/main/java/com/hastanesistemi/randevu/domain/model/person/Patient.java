package com.hastanesistemi.randevu.domain.model.person;

import com.hastanesistemi.randevu.domain.model.notification.Notifiable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hasta rolundeki kullaniciyi temsil eder.
 * <p>
 * OOP ilkesi: KALITIM - Person sinifindan turetilmistir (extends Person);
 * ad, soyad, tcKimlikNo, email gibi alanlari VE getTamAd() davranisini
 * tekrar yazmadan kullanir.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - Person'daki SOYUT metotlari (getRol,
 * getYetkiAciklamasi) KENDI SEKLINDE uygular (override). Ayrica Notifiable
 * ARAYUZUNU de kendine ozgu bir sekilde uygular (e-posta simulasyonu).
 * <p>
 * OOP ilkesi: KAPSULLEME - alerjiler listesi disariya DOGRUDAN degil,
 * degistirilemez (unmodifiable) bir görünüm olarak sunulur; listeye eleman
 * eklemenin tek yolu alerjiEkle() metodudur.
 */
public class Patient extends Person implements Notifiable {

    private String kanGrubu;
    private String sigortaNo;
    private final List<String> alerjiler = new ArrayList<>();

    public Patient(Long id, String ad, String soyad, String tcKimlikNo, String email,
                   String telefonNo, LocalDate dogumTarihi, String kanGrubu, String sigortaNo) {
        super(id, ad, soyad, tcKimlikNo, email, telefonNo, dogumTarihi);
        this.kanGrubu = kanGrubu;
        this.sigortaNo = sigortaNo;
    }

    @Override
    public Role getRol() {
        return Role.HASTA;
    }

    @Override
    public String getYetkiAciklamasi() {
        return "Uygun doktor ve islem secerek randevu olusturabilir, kendi randevularini goruntuleyebilir ve iptal edebilir.";
    }

    @Override
    public String bildirimGonder(String mesaj) {
        return "[E-POSTA -> " + getEmail() + "] " + mesaj;
    }

    public void alerjiEkle(String alerji) {
        if (alerji != null && !alerji.isBlank()) {
            alerjiler.add(alerji);
        }
    }

    public List<String> getAlerjiler() {
        return Collections.unmodifiableList(alerjiler);
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    public String getSigortaNo() {
        return sigortaNo;
    }

    public void setSigortaNo(String sigortaNo) {
        this.sigortaNo = sigortaNo;
    }
}
