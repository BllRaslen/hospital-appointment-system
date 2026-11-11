# OOP Prensiplerinin Projedeki Uygulanışı

Bu doküman, dersin istediği "her OOP özelliğinin nerede ve nasıl kullanıldığı"
açıklamasını karşılar. Kod içindeki ilgili sınıflarda da aynı açıklamalar
Türkçe Javadoc yorumu olarak tekrarlanmıştır — buradaki liste, sunum/rapor
hazırlarken hızlı referans olması için derlenmiştir.

Tüm örnekler `backend/src/main/java/com/hastanesistemi/randevu/domain/` altındadır.

## 1. Kapsülleme (Encapsulation)

> **Tanım:** Bir nesnenin iç durumunun doğrudan dışarıdan değiştirilememesi;
> değişikliğin sadece nesnenin kendi sağladığı kontrollü metotlarla yapılabilmesi.

| Sınıf | Nasıl uygulanıyor |
|---|---|
| `Appointment` | `durum` alanının **setter'ı yoktur**. Durum değişimi sadece `onayla()`, `iptalEt()`, `tamamla()` metotlarıyla yapılabilir; bu metotlar geçersiz durum geçişlerinde `InvalidAppointmentStateException` fırlatır. |
| `Person` | `ad`, `soyad`, `tcKimlikNo` alanları `final` — oluşturulduktan sonra değiştirilemez. `email`/`telefonNo` ise sadece format doğrulaması yapan setter'lar üzerinden güncellenebilir. |
| `Address`, `TimeSlot`, `WorkingHours` | Tamamen **değişmez (immutable)** value object'ler — tüm alanlar `private final`, hiç setter yok. |
| `Doctor`, `Patient` | İç koleksiyonlar (`sunduguHizmetler`, `alerjiler`, `uygunlukZamanlari`) dışarıya `Collections.unmodifiableList(...)` ile sunulur; eklemenin tek yolu sınıfın kendi metotlarıdır (`hizmetEkle`, `alerjiEkle`). |

## 2. Kalıtım (Inheritance)

> **Tanım:** Bir sınıfın başka bir sınıfın alan ve davranışlarını yeniden
> yazmadan miras alması (`extends`).

```
Person (abstract)
 ├── Patient
 └── Employee (abstract)
      ├── Doctor
      ├── Staff
      └── FacilityManager

HealthcareFacility (abstract)
 ├── Hospital
 └── Clinic

DomainException (abstract)
 ├── AppointmentConflictException
 ├── InvalidAppointmentStateException
 └── InvalidAvailabilityException
```

`Person -> Employee -> Doctor` örneği **çok seviyeli (multilevel) kalıtım**
gösterir: `Doctor`, hem `Person`'dan (ad, soyad, tcKimlikNo...) hem de
`Employee`'den (calisanNo, temelMaas, departman...) miras alır, tekrar
yazmaz.

## 3. Çok Biçimlilik (Polymorphism)

> **Tanım:** Aynı metot imzasının, farklı alt sınıflarda farklı şekillerde
> çalışması (çalışma zamanında / runtime'da hangi uygulamanın çalışacağının
> belirlenmesi — dynamic dispatch).

| Soyut metot | Tanımlandığı yer | Her alt sınıfta farklı davranışı |
|---|---|---|
| `getRol()` / `getYetkiAciklamasi()` | `Person` | Patient → HASTA, Doctor → DOKTOR, Staff → SAGLIK_PERSONELI, FacilityManager → YONETICI |
| `aylikEkOdemeHesapla()` | `Employee` | Doctor: tamamlanan randevu × 50 TL · Staff: sabit 500 TL · FacilityManager: temel maaşın %10'u |
| `getTesisTuru()` / `getGunlukMaksimumRandevuKapasitesi()` | `HealthcareFacility` | Hospital: bölüm sayısı × 40 · Clinic: muayene odası sayısı × 15 |
| `bildirimGonder(String)` | `Notifiable` (arayüz) | Patient → e-posta simülasyonu · Doctor → SMS simülasyonu |

`AppointmentDomainTest.cokBicimlilik_calisanTurlerineGoreFarkliEkOdemeHesaplar()`
testi, bir `List<Employee>` üzerinde gezinerek her nesnenin **gerçek
(runtime) türüne göre** farklı hesap yaptığını kanıtlar — bu, polimorfizmin
klasik kanıt yöntemidir.

Ayrıca `Employee.toplamAylikOdemeHesapla()` metodu basit bir **Şablon Metot
(Template Method)** örneğidir: sabit kısmı (`temelMaas + ekOdeme`) üst
sınıfta tanımlı, değişken kısmı (`aylikEkOdemeHesapla()`) alt sınıfa
bırakılmıştır.

## 4. Soyutlama (Abstraction)

> **Tanım:** Karmaşık detayları gizleyip sadece gerekli sözleşmeyi (ne
> yapıldığını, nasıl yapıldığını değil) dışa açmak.

- `Person`, `Employee`, `HealthcareFacility`, `DomainException` → **soyut
  sınıflar** (`abstract class`); doğrudan örneği oluşturulamaz
  (`new Person(...)` derleme hatası verir).
- `Notifiable` → **arayüz (interface)**; "bildirim gönderebilen" olmanın ne
  anlama geldiğini tanımlar, nasıl gönderileceğini tanımlamaz.
- Clean Architecture'ın kendisi de bir soyutlama uygulamasıdır: `application`
  katmanı, `infrastructure`'ın somut veritabanı/HTTP detaylarını bilmeden,
  sadece **port arayüzleri** (örn. gelecek güncellemede eklenecek
  `PatientRepository` arayüzü) üzerinden çalışacaktır.

## 5. Kompozisyon (Composition) — Kalıtımın Tamamlayıcısı

> **Tanım:** Bir sınıfın başka sınıfları "içermesi" (has-a), "olması"
> (is-a, kalıtım) değil.

- `Appointment`, `Patient`/`Doctor`/`MedicalService`/`HealthcareFacility`/
  `TimeSlot` nesnelerini **referans olarak içerir** — bir randevu bir hasta
  değildir, bir hastaya **sahiptir**.
- `HealthcareOrganization`, birden fazla `HealthcareFacility`yi içerir —
  çok şubeli yapı bu kompozisyon ilişkisiyle modellenmiştir.

## 6. Sınıf Yapısı / Nesne Tasarımı

Her sınıf **tek bir sorumluluğa** sahiptir (Single Responsibility):
`MedicalService` sadece hizmet bilgisini, `WorkingHours` sadece bir zaman
aralığını, `Appointment` sadece randevu yaşam döngüsünü yönetir. Yapıcı
metotlar (constructor) içinde geçersiz nesne oluşumunu engelleyen doğrulamalar
(invariant kontrolü) bulunur — örn. `Person` constructor'ı geçersiz TC Kimlik
No ile nesne oluşturulmasına izin vermez.

## 7. Tasarım Desenleri (Application Katmanı)

Domain katmanındaki OOP ilkelerinin yanı sıra, `application/` katmanında üç
klasik tasarım deseni kullanılmıştır — hepsi `backend/src/main/java/com/hastanesistemi/randevu/application/` altındadır:

| Desen | Sınıf(lar) | Neden |
|---|---|---|
| **Repository** | `port.out.{Patient,Doctor,MedicalService,HealthcareFacility,Appointment}Repository` | Application katmanı, verinin PostgreSQL'de mi yoksa bellekte mi saklandığını bilmez; sadece bu arayüzleri bilir. Bağımlılık Kuralı'nın (Dependency Rule) somut uygulaması — bkz. `RandevuServisiTest` + `SahteRandevuDeposu`, gerçek bir veritabanı olmadan test edilebildiğini kanıtlar. |
| **Factory Method** | `factory.EmployeeFactory` | `Role` enumuna göre `Doctor`/`Staff`/`FacilityManager`'dan hangisinin örnekleneceği kararı, çağıran koddan (ileride bir personel kayıt servisi/controller) ayrıştırılır. |
| **Strategy** | `pricing.UcretlendirmeStrategy` + `StandartUcretlendirmeStrategy` + `SigortaliUcretlendirmeStrategy` | `Employee.aylikEkOdemeHesapla()` kalıtım ile polimorfizm sağlıyordu (her ALT SINIF sabit bir kurala bağlı); burada ise aynı hesaplama türünün (ücretlendirme) birden fazla algoritması, kalıtım kullanmadan, **çalışma zamanında enjekte edilerek** (kompozisyon) değiştirilebilir. "Kalıtım yerine kompozisyonu tercih et" ilkesinin klasik örneği. |

`application.service.RandevuServisi`, bu üç deseni ve domain katmanının
kapsülleme/çok biçimlilik ilkelerini (durum geçişleri, `Notifiable` arayüzü)
bir araya getiren use-case servisidir: `randevuOlustur` → doktor uygunluğu
(`Doctor.zamanUygunMu`) ve çakışma (`Appointment.cakisiyorMu`) kontrolü;
`randevuOnayla`/`randevuIptalEt`/`randevuTamamla` → domain'in kendi durum
makinesi; `efektifUcretHesapla` → enjekte edilen `UcretlendirmeStrategy`.

## 8. JPA Eşlemeleri ve Adapter Deseni (Infrastructure Katmanı)

`infrastructure/persistence/` altında, domain modelini PostgreSQL'e
eşleyen bir JPA katmanı eklendi. Bu katmanın en önemli OOP/mimari dersi:
**domain sınıflarının hiçbirine tek bir JPA anotasyonu bile eklenmedi** —
Clean Architecture'ın Bağımlılık Kuralı korunuyor. Bunun yerine, `entity/`
altında domain sınıflarıyla birebir eşleşen PARALEL bir sınıf hiyerarşisi
tanımlandı:

| Desen/Teknik | Nerede | Neden |
|---|---|---|
| **MappedSuperclass + SINGLE_TABLE kalıtım** | `PersonEntity` (mapped superclass) → `PatientEntity`; `EmployeeEntity` (`@Inheritance(SINGLE_TABLE)`) → `DoctorEntity`/`StaffEntity`/`FacilityManagerEntity`; aynı desen `HealthcareFacilityEntity` → `HospitalEntity`/`ClinicEntity` için tekrarlanır | Domain'deki kalıtım hiyerarşisi (bkz. bölüm 2), veritabanı şemasında discriminator kolonuyla (`calisan_turu`, `tesis_turu`) birebir yansıtılır — tek tabloda tüm alt türler, polimorfik sorgulanabilir şekilde saklanır. |
| **Adapter** | `adapter.{Patient,Doctor,MedicalService,HealthcareFacility,Appointment}RepositoryAdapter` | Her biri, application katmanındaki bir port arayüzünü (bkz. bölüm 7 — Repository deseni) Spring Data JPA'nın somut API'siyle GERÇEKLEŞTİRİR. `RandevuServisi` bu adapter sınıflarının adını hiç bilmez; Spring bunları runtime'da enjekte eder. Bu, Dependency Inversion'ın üretim kodunda (gerçek veritabanıyla) çalıştığının kanıtıdır — bir önceki günün testlerinde bu rolü `SahteRandevuDeposu` oynuyordu. |
| **Mapper (durumsuz dönüştürücü)** | `mapper.*Mapper` | `toEntity`/`toDomain` statik metotları, iki dünya (domain/JPA) arasındaki TEK geçiş noktasıdır. `HealthcareFacilityMapper`, tesisin gerçek türüne (`instanceof Hospital`/`Clinic`) göre doğru somut sınıfı seçerek mapper seviyesinde de çok biçimliliği sergiler. |

Bilinçli bir JPA tuzağına da burada deginmek gerekir: `Appointment` bir
`Patient`/`Doctor`/`MedicalService`/`HealthcareFacility`'ye (cross-aggregate)
referans verir. Mapper bu referanslar için YENİ bir entity İNŞA ETMEZ —
aksi halde Hibernate zaten var olan bir kaydı tekrar eklemeye çalışır
("transient instance" hatası). Bunun yerine **adapter**, ilgili diğer
repository'den `getReferenceById(id)` ile (ekstra sorgu olmadan) var olan
kaydın referansını alır ve mapper'a hazır olarak verir — cross-aggregate
referans çözümlemesi bilinçli olarak adapter'in sorumluluğundadır, mapper'ın
değil.

`AppointmentMapperTest`/`DoctorMapperTest`/`HealthcareFacilityMapperTest`/
`PatientMapperTest`, mapper'ların PostgreSQL veya Spring bağlamı
(ApplicationContext) olmadan, saf Java nesneleriyle test edilebildiğini
kanıtlar — mapper'lar durumsuz oldukları için testleri de Gün 1/2'deki
domain/application testleri kadar hızlıdır.

## 9. Spring Security + JWT (Infrastructure Katmanı)

`infrastructure/security/` altında, kimlik doğrulamanın (authentication)
**oturumsuz (stateless)** ve JWT tabanlı olarak nasıl kurulduğu açıklanır.
Buradaki en önemli mimari karar: `sifreHash` alanı **domain sınıfı
`Person`'a hiç eklenmedi**, sadece `PersonEntity`'ye eklendi — kimlik
doğrulama bir iş kuralı değil, bir altyapı/güvenlik detayıdır; Bağımlılık
Kuralı domain'in bundan habersiz kalmasını gerektirir.

| Desen/Teknik | Nerede | Neden |
|---|---|---|
| **Adapter** | `RandevuKullanicisi` | `PersonEntity` hiyerarşisini (`PatientEntity`/`DoctorEntity`/`StaffEntity`/`FacilityManagerEntity`) Spring Security'nin `UserDetails` sözleşmesine bağlar. Spring Security hiçbir zaman proje entity'lerini doğrudan görmez. |
| **Çok biçimlilik (`instanceof` çözümlemesi)** | `RandevuKullanicisi.calisandanOlustur(EmployeeEntity)` | `calisanlar` tablosu SINGLE_TABLE kalıtımla polimorfik sorgulandığı için (bkz. bölüm 8), kendisine verilen entity'nin GERÇEK alt türüne göre doğru `Role`'ü (DOKTOR/SAGLIK_PERSONELI/YONETICI) seçer. |
| **Kapsülleme** | `JwtServisi` | jjwt kütüphanesinin ayrıştırma (parsing) detayları ve gizli anahtar tamamen `private`'tır; dışa sadece `tokenUret`/`kullaniciAdiniCikar`/`tokenGecerliMi` gibi anlamlı işlemler açılır. |
| **Şablon Metot (Template Method)** | `JwtKimlikDogrulamaFiltresi extends OncePerRequestFilter` | Spring Security'nin sağladığı "filtre zincirinde bir kez çalış" iskeletine, sadece değişken parça (`doFilterInternal`) eklenir — `Employee.toplamAylikOdemeHesapla()` ile aynı ilke. |
| **Kompozisyon** | `GuvenlikYapilandirmasi` | Kendi başına kimlik doğrulama mantığı içermez; `KullaniciDetayServisi`, `PasswordEncoder` (BCrypt) ve `JwtKimlikDogrulamaFiltresi`'ni enjekte edilen parçalar olarak bir araya getirir (bkz. bölüm 7 — Strategy ile aynı "kalıtım yerine kompozisyon" mantığı). |

Sistemde "tek bir kullanıcılar tablosu" yoktur (hasta ve çalışanlar ayrı
tablolarda tutulur, bkz. bölüm 8); `KullaniciDetayServisi`
(`UserDetailsService`), e-posta ile önce hastalarda, bulamazsa
`EmployeeJpaRepository` ile (Doktor/SağlıkPersoneli/Yönetici'yi TEK sorguyla
polimorfik olarak döndüren) çalışanlarda arama yaparak bu ayrımı kimlik
doğrulama katmanından gizler.

`JwtServisiTest`/`RandevuKullanicisiTest`, bu katmanın da (mapper'lar gibi)
Spring `ApplicationContext` veya PostgreSQL olmadan, saf Java nesneleriyle
test edilebildiğini kanıtlar.

## 10. Presentation Katmanı (REST Controller'lar)

`presentation/` altında, application/domain katmanlarını HTTP'ye açan "ince"
controller'lar eklendi. Buradaki en önemli mimari karar: controller'lar
**hiçbir iş kuralı içermez** — `RandevuServisi`/`Appointment`/`Doctor`'daki
kuralların (çakışma, durum geçişi, uygunluk) sadece HTTP'ye çevirisini yapar.

| Desen/Teknik | Nerede | Neden |
|---|---|---|
| **Çok biçimlilik (polimorfik yakalama)** | `GlobalHataYakalayici.cakismaVeDurumHatalari` | Tek bir `@ExceptionHandler`, `DomainException`'ın BİRDEN FAZLA alt sınıfını (`AppointmentConflictException`, `InvalidAppointmentStateException`) tek seferde yakalar — bkz. bölüm 3 (çok biçimlilik) ve `DomainException` ic dokümantasyonu. |
| **DTO (veri taşıyıcı) + fromDomain** | `presentation.dto.*` (`HastaYanit`, `DoktorYanit`, `HizmetYanit`, `TesisYanit`, `RandevuYanit`) | Domain nesneleri HTTP/JSON'a DOĞRUDAN serileştirilmez — her DTO'nun statik `fromDomain` metodu, hangi alanların dışa açılacağını (örn. şifre hash'inin ASLA dışa açılmaması) tek bir yerde kontrol eder. |
| **Factory Method (yeniden kullanım)** | `AuthController.doktorKayitOl` | Bölüm 7'deki `EmployeeFactory`, presentation katmanından da çağrılır — bir yöneticinin yeni doktor hesabı açması akışında HANGİ somut `Employee` alt sınıfının üretileceği kararı hâlâ factory'dedir, controller bunu bilmez. |
| **Framework'ten bağımsız application katmanının Spring'e bağlanması** | `infrastructure.config.UygulamaServisleriYapilandirmasi` | `RandevuServisi`'nin KENDİSİ hiçbir Spring anotasyonu içermez (bölüm 7); bu `@Configuration` sınıfı, `@Bean` fabrika metoduyla onu Spring bağlamına (context) bağlar. "Framework'e bağlama" sorumluluğu, Bağımlılık Kuralı'na uygun şekilde EN DIŞ katmanda toplanır. |
| **Yetki bazlı erişim kontrolü** | `@PreAuthorize` (`RandevuController`, `AuthController.doktorKayitOl`, `HastaController`) | `Role` enumuna (bölüm 1/3) karşılık gelen `ROLE_*` yetkileri (`RandevuKullanicisi.getAuthorities`, bölüm 9), metot seviyesinde (`@EnableMethodSecurity`) kontrol edilir — ör. sadece `ROLE_HASTA` randevu oluşturabilir, sadece `ROLE_DOKTOR`/`ROLE_SAGLIK_PERSONELI` onaylayabilir/tamamlayabilir. |

`RandevuController.randevuIptalEt` ayrıca domain SEVIYESINDE olmayan, ama
güvenlik için gerekli bir iş kuralı uygular: bir HASTA sadece KENDİ
randevusunu iptal edebilir (randevunun `hasta.id`'si ile oturum açan
kullanıcının `kullaniciId`'si karşılaştırılır); doktor/personel/yönetici
herhangi bir randevuyu iptal edebilir. Bu kontrol bilinçli olarak
controller'da yapılır, çünkü "kim kimin verisine erişebilir" bir yetkilendirme
(authorization) detayıdır, randevunun kendi iç tutarlılığıyla (bölüm 1'deki
`Appointment` kapsüllemesi) ilgili değildir.

`DtoDonusumTest`, mapper testleriyle (bölüm 8) aynı felsefeyle, her
`...Yanit.fromDomain` dönüşümünün alanları doğru eşlediğini kanıtlar;
`GlobalHataYakalayiciTest` ise her istisna türünün doğru HTTP durum koduna
çevrildiğini, Spring `ApplicationContext` başlatmadan doğrular.

## Devam Edecek Çalışma

Bu doküman, frontend (Next.js) giriş/kayıt ekranları ve randevu oluşturma
akışı tamamlandığında tekrar güncellenecektir.
