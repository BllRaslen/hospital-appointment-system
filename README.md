# Hastane / Klinik Randevu Sistemi

Nesneye Dayalı Programlama (NDP/OOP) dönem projesi — **Seçenek A: Hastane/Klinik Randevu Sistemi Geliştirme**.

Platform bağımsız bir OOP konsepti olarak; hem tek bir klinik/hastane hem de
çok şubeli sağlık kuruluşlarını destekleyebilen bir randevu sistemi
geliştirilmektedir.

## Teknoloji Yığını

| Katman        | Teknoloji                                  |
|---------------|---------------------------------------------|
| Frontend      | Next.js (TypeScript)                         |
| Backend       | Spring Boot (Java 17)                        |
| Veritabanı    | PostgreSQL                                   |
| Mimari        | Clean Architecture (Domain / Application / Infrastructure / Presentation) |
| Kimlik Doğrulama | Spring Security + JWT                    |

## Mimari Yaklaşım — Clean Architecture

Proje, Robert C. Martin'in Clean Architecture prensiplerine göre katmanlara
ayrılmıştır. **Bağımlılık Kuralı**: dış katmanlar iç katmanlara bağımlı
olabilir, iç katmanlar (domain) hiçbir dış katmana veya framework'e bağımlı
OLAMAZ.

```
backend/src/main/java/com/hastanesistemi/randevu/
├── domain/           # Çekirdek iş kuralları — framework'ten bağımsız saf Java
│   ├── model/        # Entity ve value object sınıfları (OOP'nin kalbi)
│   └── exception/    # İş kuralı istisnaları
├── application/      # Use-case'ler, port arayüzleri (Repository/Factory/Strategy desenleri)
├── infrastructure/   # JPA entity/adapter'ları + PostgreSQL yapılandırması + Spring Security/JWT + CORS
└── presentation/     # REST controller'ları + DTO'lar + GlobalHataYakalayici
```

```
frontend/src/
├── app/              # Next.js App Router sayfaları (presentation)
├── domain/           # API sözleşmesini temsil eden TypeScript tipleri
├── application/      # (yakında) use-case hook'ları
├── infrastructure/   # API istemcisi (apiClient.ts)
└── presentation/      # (yakında) React bileşenleri
```

## Çok Şubeli Yapı İçin Veri Modeli

Tüm şubeler **aynı** PostgreSQL veritabanında tutulur; her tablo
`hospital_id` (facility id) yabancı anahtarı taşır. Birden fazla şubesi olan
bir sağlık kuruluşu, `HealthcareOrganization` sınıfı altında birden fazla
`HealthcareFacility` (Hospital/Clinic) barındırır. Tek şubeli bir klinik için
`HealthcareOrganization` gerekmez.

## Kullanıcı Rolleri

1. **Hasta** — randevu oluşturur, görüntüler, iptal eder.
2. **Doktor / Sağlık Personeli** — kendisine atanan randevuları onaylar/tamamlar, uygunluk saatlerini yönetir.
3. **Hastane / Klinik Yöneticisi** — tesis, bölüm, çalışan ve hizmet tanımlarını yönetir.

## OOP Prensiplerinin Uygulanışı

Her OOP prensibinin hangi sınıfta ve neden kullanıldığının ayrıntılı açıklaması
[`docs/OOP-PRENSIPLERI.md`](docs/OOP-PRENSIPLERI.md) dosyasındadır. Kod
içindeki Türkçe Javadoc yorumları da aynı açıklamaları sınıf/metot seviyesinde
tekrarlar.

## Geliştirme Durumu (Günlük İlerleme)

Bu proje, gerçek geliştirme sürecini yansıtan ayrı günlerde yapılan commit'lerle
ilerletilmektedir (bkz. proje teslim gereksinimleri — en az 10 farklı günde commit).

- [x] **Gün 1 (2026-11-06):** Proje iskeleti (monorepo, backend/frontend klasör yapısı), backend domain katmanı (Person/Employee/Doctor/Staff/FacilityManager, HealthcareFacility/Hospital/Clinic, Appointment, MedicalService, value object'ler, istisnalar) ve birim testleri.
- [x] **Gün 2 (2026-11-07):** Application katmanı — repository port arayüzleri (Repository deseni), `EmployeeFactory` (Factory Method deseni), `UcretlendirmeStrategy` ve implementasyonları (Strategy deseni), `RandevuServisi` (randevu oluşturma/onaylama/iptal/tamamlama use-case'leri) ve sahte (in-memory) repository ile birim testleri.
- [x] **Gün 3 (2026-11-08):** Infrastructure katmanı — JPA entity eşlemeleri (`PersonEntity`/`EmployeeEntity`/`HealthcareFacilityEntity` SINGLE_TABLE kalıtım stratejileri, embeddable'lar), Spring Data JPA repository arayüzleri, application port'larını gerçekleştiren adapter sınıfları (Adapter deseni), PostgreSQL datasource/JPA yapılandırması (`application.yml`) ve mapper birim testleri.
- [x] **Gün 4 (2026-11-09):** Spring Security + JWT yapılandırması — `PersonEntity`'ye kimlik doğrulama için `sifreHash` alanı, `RandevuKullanicisi` (`UserDetails` adapter'ı, Doctor/Staff/FacilityManager için çok biçimli rol çözümlemesi), `KullaniciDetayServisi` (`UserDetailsService`), `JwtServisi` (JWT üretimi/doğrulanması), `JwtKimlikDogrulamaFiltresi` (`OncePerRequestFilter`), `GuvenlikYapilandirmasi` (oturumsuz/stateless `SecurityFilterChain`, BCrypt `PasswordEncoder`) ve bu katmanın birim testleri.
- [x] **Gün 5 (2026-11-11):** Presentation katmanı — `AuthController` (giriş/kayıt/kayıt-doktor), `RandevuController` (oluştur/onayla/iptal/tamamla/listele), `DoktorController`/`HizmetController`/`TesisController`/`HastaController` (listeleme), istek/yanıt DTO'ları, `GlobalHataYakalayici` (`DomainException` → HTTP durum kodu eşlemesi), `KayitServisi` (şifre hash'leme), `UygulamaServisleriYapilandirmasi` (`RandevuServisi` bean wiring), `CorsYapilandirmasi` (Next.js için CORS) ve bu katmanın birim testleri.
- [ ] Frontend: giriş/kayıt ekranları, randevu oluşturma akışı
- [ ] Frontend: doktor/yönetici panelleri
- [ ] Entegrasyon testleri ve son rötuşlar

## Çalıştırma (Backend)

```bash
cd backend
mvn spring-boot:run
```

> Not: `mvn spring-boot:run` için çalışan bir PostgreSQL veritabanı
> gereklidir (bkz. `application.yml` — `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`
> ortam değişkenleriyle yapılandırılır). REST API, `/api/auth/giris`,
> `/api/auth/kayit`, `/api/doktorlar`, `/api/hizmetler`, `/api/tesisler`,
> `/api/randevular/**` uç noktalarını `http://localhost:8080` üzerinde açar
> (bkz. `presentation/` paketi). Veritabanı olmadan, domain +
> application + infrastructure/mapper birim testleri (Spring context
> başlatılmadan, saf Java ile) çalıştırılabilir:
> ```bash
> cd backend
> mvn test
> ```

## Çalıştırma (Frontend)

```bash
cd frontend
npm install
npm run dev
```

Tarayıcıda `http://localhost:3000` adresini açın.
