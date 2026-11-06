package com.hastanesistemi.randevu.domain.model.person;

/**
 * Sistemdeki kullanici rollerini temsil eder. Gereksinim: "En az uc rol
 * onerilir: Hasta, Doktor / Saglik Personeli, Hastane / Klinik Yoneticisi."
 * <p>
 * Bu projede dort rol kullaniyoruz (DOKTOR ve SAGLIK_PERSONELI ayri siniflarla
 * modellenerek cok biciminligi daha zengin gostermek icin ayristirildi), ancak
 * yetkilendirme (Spring Security) tarafinda SAGLIK_PERSONELI ile DOKTOR benzer
 * izin seviyesine sahip olabilir.
 */
public enum Role {
    HASTA,
    DOKTOR,
    SAGLIK_PERSONELI,
    YONETICI
}
