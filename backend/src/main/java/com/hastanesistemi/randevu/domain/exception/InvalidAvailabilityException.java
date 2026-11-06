package com.hastanesistemi.randevu.domain.exception;

/**
 * Bir hasta, doktorun tanimli uygunluk (calisma) zaman araliklarinin disinda
 * bir saat icin randevu talep ettiginde firlatilir. Gereksinim: "Hastalar
 * sadece bu zaman araliklarina randevu alabilecek."
 */
public class InvalidAvailabilityException extends DomainException {

    public InvalidAvailabilityException(String message) {
        super(message);
    }
}
