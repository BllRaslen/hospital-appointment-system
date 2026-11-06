/**
 * ALTYAPI (Infrastructure) katmani.
 * <p>
 * Veritabani (JPA entity eslemeleri, Spring Data repository implementasyonlari,
 * PostgreSQL baglantisi), guvenlik (Spring Security + JWT yapilandirmasi) ve
 * diger dis sistem entegrasyonlari burada yer alacaktir. Bu katman,
 * application katmaninda tanimlanan "port" arayuzlerini (ornegin
 * PatientRepository) GERCEKLESTIRIR (implements).
 * <p>
 * Durum: PostgreSQL baglantisi, JPA entity/repository siniflari ve
 * Spring Security/JWT yapilandirmasi ilerleyen gunlerde eklenecektir.
 */
package com.hastanesistemi.randevu.infrastructure;
