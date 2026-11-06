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
 * Durum: Bu katmanin ic icerigi, domain modeli tamamlandiktan sonraki
 * gunlerde (use case servisleri, DTO'lar, port arayuzleri) eklenecektir.
 */
package com.hastanesistemi.randevu.application;
