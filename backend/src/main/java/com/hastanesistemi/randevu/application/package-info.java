/**
 * UYGULAMA (Application / Use Case) katmani.
 * <p>
 * Clean Architecture'da bu katman, domain nesnelerini kullanarak is
 * senaryolarini (use case) yurutur: "randevu olustur", "randevuyu onayla",
 * "doktor uygunlugu sorgula" gibi. Domain'e bagimlidir ama infrastructure
 * veya presentation'a BAGIMLI DEGILDIR; disariyla iletisimi "port" (arayuz)
 * adi verilen soyutlamalar uzerinden kurar (ornegin bir PatientRepository
 * arayuzu burada tanimlanir, gercek veritabani implementasyonu ise
 * infrastructure katmaninda yazilir).
 * <p>
 * Alt paketler:
 * <ul>
 *   <li>{@code port.out} - repository PORT arayuzleri (Repository deseni)</li>
 *   <li>{@code factory} - nesne olusturma mantigini merkezilestiren fabrikalar
 *       (Factory Method deseni, bkz. {@code EmployeeFactory})</li>
 *   <li>{@code pricing} - degistirilebilir ucretlendirme algoritmalari
 *       (Strategy deseni, bkz. {@code UcretlendirmeStrategy})</li>
 *   <li>{@code service} - use-case servisleri (bkz. {@code RandevuServisi})</li>
 * </ul>
 * <p>
 * Durum: infrastructure (JPA/PostgreSQL implementasyonlari) ve presentation
 * (REST controller) katmanlari eklendiginde, buradaki port arayuzleri ilgili
 * somut siniflar tarafindan implemente edilecek/cagrilacaktir.
 */
package com.hastanesistemi.randevu.application;
