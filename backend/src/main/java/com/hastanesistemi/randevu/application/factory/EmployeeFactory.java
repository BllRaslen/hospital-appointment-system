package com.hastanesistemi.randevu.application.factory;

import com.hastanesistemi.randevu.domain.model.person.Employee;
import com.hastanesistemi.randevu.domain.model.person.Role;

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
}
