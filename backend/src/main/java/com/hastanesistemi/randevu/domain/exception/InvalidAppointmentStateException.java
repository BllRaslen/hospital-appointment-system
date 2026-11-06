package com.hastanesistemi.randevu.domain.exception;

/**
 * Bir randevu uzerinde durumuyla uyumsuz bir islem yapilmaya calisildiginda
 * firlatilir. Ornek: zaten TAMAMLANDI durumundaki bir randevuyu tekrar
 * onaylamaya calismak.
 * <p>
 * Bu istisna, Appointment sinifinin durum (state) gecislerini KAPSULLEME
 * (encapsulation) ile korumasinin bir sonucudur - durum alani disaridan
 * dogrudan degistirilemez, sadece onayla()/iptalEt()/tamamla() gibi kontrollu
 * metodlarla degistirilebilir ve bu metodlar gecersiz gecisleri bu istisna
 * ile reddeder.
 */
public class InvalidAppointmentStateException extends DomainException {

    public InvalidAppointmentStateException(String message) {
        super(message);
    }
}
