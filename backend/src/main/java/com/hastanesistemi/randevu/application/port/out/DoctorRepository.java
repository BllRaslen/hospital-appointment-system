package com.hastanesistemi.randevu.application.port.out;

import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Specialty;

import java.util.List;
import java.util.Optional;

/**
 * Doktor nesnelerinin kalici hale getirilmesi/sorgulanmasi icin port.
 * Bkz. {@link PatientRepository} ic dokumantasyonu (Repository deseni).
 */
public interface DoctorRepository {

    Doctor kaydet(Doctor doktor);

    Optional<Doctor> idIleBul(Long id);

    List<Doctor> uzmanlikAlaninaGoreBul(Specialty uzmanlikAlani);

    List<Doctor> hepsiniGetir();
}
