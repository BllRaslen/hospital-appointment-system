package com.hastanesistemi.randevu.application.factory;

import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Employee;
import com.hastanesistemi.randevu.domain.model.person.FacilityManager;
import com.hastanesistemi.randevu.domain.model.person.Role;
import com.hastanesistemi.randevu.domain.model.person.Staff;

/**
 * Rol bilgisine gore dogru {@link Employee} alt sinifini olusturan fabrika.
 * Gereksinim: "Doktor ve saglik personeli sisteme kayit edilebilecek" —
 * personel kaydi akisinin (ileride bir REST controller) HANGI somut sinifi
 * ({@code new Doctor(...)}, {@code new Staff(...)}, {@code new
 * FacilityManager(...)}) cagiracagini bilmesine gerek kalmaz.
 * <p>
 * OOP ilkesi: FACTORY METHOD DESENI (tasarim deseni) - nesne olusturma
 * mantigi, cagiran koddan (ornegin ileride yazilacak bir
 * PersonelKayitServisi) ayristirilir ve tek bir yerde merkezilestirilir.
 * Bu, SOYUTLAMA ilkesinin bir uzantisidir: cagiran kod sadece soyut
 * {@code Employee} tipini ve {@code Role} enumunu bilir, somut siniflarin
 * constructor imzalarini bilmek zorunda degildir.
 */
public final class EmployeeFactory {

    private EmployeeFactory() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static Employee olustur(Role rol, EmployeeOlusturmaBilgisi bilgi) {
        if (rol == null) {
            throw new IllegalArgumentException("Rol bilgisi bos olamaz.");
        }
        if (bilgi == null) {
            throw new IllegalArgumentException("Calisan olusturma bilgisi bos olamaz.");
        }
        return switch (rol) {
            case DOKTOR -> new Doctor(bilgi.id(), bilgi.ad(), bilgi.soyad(), bilgi.tcKimlikNo(),
                    bilgi.email(), bilgi.telefonNo(), bilgi.dogumTarihi(), bilgi.calisanNo(),
                    bilgi.iseBaslamaTarihi(), bilgi.temelMaas(), bilgi.uzmanlikAlani(), bilgi.diplomaNo());
            case SAGLIK_PERSONELI -> new Staff(bilgi.id(), bilgi.ad(), bilgi.soyad(), bilgi.tcKimlikNo(),
                    bilgi.email(), bilgi.telefonNo(), bilgi.dogumTarihi(), bilgi.calisanNo(),
                    bilgi.iseBaslamaTarihi(), bilgi.temelMaas(), bilgi.unvan());
            case YONETICI -> new FacilityManager(bilgi.id(), bilgi.ad(), bilgi.soyad(), bilgi.tcKimlikNo(),
                    bilgi.email(), bilgi.telefonNo(), bilgi.dogumTarihi(), bilgi.calisanNo(),
                    bilgi.iseBaslamaTarihi(), bilgi.temelMaas());
            case HASTA -> throw new IllegalArgumentException(
                    "HASTA rolu bir Employee degildir; Patient nesnesi dogrudan olusturulmalidir.");
        };
    }
}
