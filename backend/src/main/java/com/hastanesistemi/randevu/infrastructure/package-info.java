/**
 * ALTYAPI (Infrastructure) katmani.
 * <p>
 * Veritabani (JPA entity eslemeleri, Spring Data repository implementasyonlari,
 * PostgreSQL baglantisi), guvenlik (Spring Security + JWT yapilandirmasi) ve
 * diger dis sistem entegrasyonlari burada yer alir. Bu katman, application
 * katmaninda tanimlanan "port" arayuzlerini (ornegin PatientRepository)
 * GERCEKLESTIRIR (implements).
 * <p>
 * Alt paketler ({@code persistence/}):
 * <ul>
 *   <li>{@code entity} - JPA {@code @Entity}/{@code @MappedSuperclass}
 *       siniflari; domain modelindeki kalitim hiyerarsisi SINGLE_TABLE
 *       stratejisiyle birebir yansitilir.</li>
 *   <li>{@code embeddable} - deger nesnelerinin (Address, WorkingHours)
 *       {@code @Embeddable} karsiliklari.</li>
 *   <li>{@code repository} - Spring Data {@code JpaRepository} arayuzleri
 *       (teknik detay; application katmani bunlari GORMEZ).</li>
 *   <li>{@code mapper} - domain &lt;-&gt; entity donusumu yapan durumsuz
 *       yardimci siniflar.</li>
 *   <li>{@code adapter} - application katmanindaki port arayuzlerini
 *       (PatientRepository, DoctorRepository, ...) GERCEKLESTIREN,
 *       {@code @Repository} ile isaretli Spring bean'leri (Adapter deseni).</li>
 * </ul>
 * <p>
 * {@code security/} alt paketinde Spring Security + JWT yapilandirmasi
 * (UserDetails adapter, UserDetailsService, JWT uretimi/dogrulanmasi,
 * istek filtresi, SecurityFilterChain) bulunur — bkz. {@code
 * com.hastanesistemi.randevu.infrastructure.security} paket dokumantasyonu.
 * <p>
 * Durum: REST controller'lar (presentation) ilerleyen gunlerde eklenecektir.
 */
package com.hastanesistemi.randevu.infrastructure;
