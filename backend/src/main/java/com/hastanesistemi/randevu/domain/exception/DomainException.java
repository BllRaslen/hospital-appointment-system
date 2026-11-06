package com.hastanesistemi.randevu.domain.exception;

/**
 * Tum domain (is kurallari) istisnalarinin temel sinifi.
 * <p>
 * Neden burada? Clean Architecture'da domain katmani disariya (framework,
 * veritabani, HTTP) bagimli olmamalidir. Bu yuzden "checked exception" yerine
 * RuntimeException tabanli, framework'den bagimsiz bir hata hiyerarsisi
 * kullaniyoruz. Presentation katmani (REST controller) bu istisnalari
 * yakalayip uygun HTTP durum koduna cevirecek (ornegin 409 Conflict).
 * <p>
 * OOP ilkesi: KALITIM (inheritance) - alt siniflar (AppointmentConflictException,
 * InvalidAppointmentStateException, InvalidAvailabilityException) bu sinifin
 * ortak davranisini (mesaj tasima) miras alir; catch (DomainException e) ile
 * tek seferde tum is kurali hatalarini yakalamak mumkun olur (polimorfik yakalama).
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
