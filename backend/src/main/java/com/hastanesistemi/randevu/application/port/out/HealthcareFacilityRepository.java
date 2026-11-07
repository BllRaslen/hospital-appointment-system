package com.hastanesistemi.randevu.application.port.out;

import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;

import java.util.List;
import java.util.Optional;

/**
 * Tesis (Hospital/Clinic) nesnelerinin kalici hale getirilmesi/sorgulanmasi
 * icin port. Bkz. {@link PatientRepository} ic dokumantasyonu (Repository
 * deseni). Donus tipi soyut {@code HealthcareFacility} oldugundan,
 * cagiran kod hangi somut tesis turuyle (Hospital/Clinic) calistigini bilmek
 * zorunda kalmaz (cok biciminlik, Repository seviyesinde).
 */
public interface HealthcareFacilityRepository {

    HealthcareFacility kaydet(HealthcareFacility tesis);

    Optional<HealthcareFacility> idIleBul(Long id);

    List<HealthcareFacility> hepsiniGetir();
}
