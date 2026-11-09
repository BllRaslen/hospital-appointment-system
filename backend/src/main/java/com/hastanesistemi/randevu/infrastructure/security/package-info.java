/**
 * GUVENLIK (Spring Security + JWT) alt paketi.
 * <p>
 * Kimlik dogrulama (authentication) OTURUMSUZ (stateless) ve JWT tabanlidir:
 * <ul>
 *   <li>{@code RandevuKullanicisi} - {@code PersonEntity} alt siniflarini
 *       ({@code PatientEntity}/{@code DoctorEntity}/{@code StaffEntity}/
 *       {@code FacilityManagerEntity}) Spring Security'nin
 *       {@code UserDetails} sozlesmesine BAGLAYAN adapter.</li>
 *   <li>{@code KullaniciDetayServisi} - {@code UserDetailsService}
 *       uygulamasi; e-posta ile hasta/calisan tablolarinda arama yapar.</li>
 *   <li>{@code JwtServisi} - JWT uretimi/dogrulanmasini kapsulleyen servis.</li>
 *   <li>{@code JwtKimlikDogrulamaFiltresi} - her istekte JWT'yi okuyup
 *       {@code SecurityContext}'i kuran filtre.</li>
 *   <li>{@code GuvenlikYapilandirmasi} - {@code SecurityFilterChain},
 *       {@code PasswordEncoder} (BCrypt) ve kimlik dogrulama saglayicisinin
 *       merkezi yapilandirmasi.</li>
 * </ul>
 * <p>
 * Durum: giris/kayit (login/register) REST endpoint'leri, ilerleyen gunde
 * presentation katmaninda eklenecektir; bu paket sadece ALTYAPIYI saglar.
 */
package com.hastanesistemi.randevu.infrastructure.security;
