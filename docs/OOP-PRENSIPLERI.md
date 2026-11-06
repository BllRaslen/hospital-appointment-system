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

## Devam Edecek Çalışma

Bu doküman, domain katmanı tamamlandıkça (application/infrastructure/
presentation katmanları eklendiğinde — örn. Strategy, Factory, Repository
desenleri) güncellenecektir.
