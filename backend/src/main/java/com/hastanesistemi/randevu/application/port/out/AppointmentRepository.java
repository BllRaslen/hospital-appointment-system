package com.hastanesistemi.randevu.application.port.out;

import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Randevu (Appointment) nesnelerinin kalici hale getirilmesi/sorgulanmasi
 * icin port. Bkz. {@link PatientRepository} ic dokumantasyonu (Repository
 * deseni).
 * <p>
 * {@code doktorunRandevulariniGetir} ozellikle onemlidir: cakisma kontrolu
 * (bkz. {@code Appointment.cakisiyorMu}) yapabilmek icin, adayla
 * KARSILASTIRILACAK mevcut randevularin once bu metotla getirilmesi
 * gerekir — gercek karsilastirma mantigi hala domain katmaninda
 * (Appointment icinde) kalir, repository sadece veriyi saglar.
 */
public interface AppointmentRepository {

    Appointment kaydet(Appointment randevu);

    Optional<Appointment> idIleBul(Long id);

    List<Appointment> doktorunRandevulariniGetir(Doctor doktor);

    List<Appointment> hastaninRandevulariniGetir(Patient hasta);

    List<Appointment> hepsiniGetir();
}
