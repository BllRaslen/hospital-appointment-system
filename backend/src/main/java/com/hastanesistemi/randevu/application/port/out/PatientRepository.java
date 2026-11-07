package com.hastanesistemi.randevu.application.port.out;

import com.hastanesistemi.randevu.domain.model.person.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Hasta (Patient) nesnelerinin kalici hale getirilmesi/sorgulanmasi icin
 * application katmaninin ihtiyac duydugu sozlesme (port).
 * <p>
 * OOP ilkesi: SOYUTLAMA + REPOSITORY DESENI - application katmani, hastanin
 * NEREDE (PostgreSQL, bellek ici, dosya...) saklandigini bilmez; sadece bu
 * arayuzu bilir. Gercek saklama detayi, bu arayuzu implemente eden bir sinif
 * tarafindan infrastructure katmaninda (veya testlerde sahte/in-memory bir
 * implementasyonla) saglanir. Bu, Clean Architecture'in Bagimlilik Kuralinin
 * ("ic katmanlar dis katmanlara bagimli OLAMAZ") somut uygulamasidir.
 */
public interface PatientRepository {

    Patient kaydet(Patient hasta);

    Optional<Patient> idIleBul(Long id);

    Optional<Patient> tcKimlikNoIleBul(String tcKimlikNo);

    List<Patient> hepsiniGetir();
}
