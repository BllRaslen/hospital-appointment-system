package com.hastanesistemi.randevu.domain.exception;

/**
 * Bir doktorun ayni zaman araliginda baska bir randevusu oldugunda firlatilir
 * (randevu cakisma kontrolu). Gereksinim: "Randevu olusturulurken cakisma
 * kontrolu yapilacak."
 * <p>
 * OOP ilkesi: KALITIM - DomainException'dan turetildi.
 */
public class AppointmentConflictException extends DomainException {

    public AppointmentConflictException(String message) {
        super(message);
    }
}
