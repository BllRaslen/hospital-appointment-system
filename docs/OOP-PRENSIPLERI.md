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

## Devam Edecek Çalışma

Bu doküman, Spring Security/JWT yapılandırması ve presentation (REST
controller) katmanı eklendiğinde güncellenecektir.
