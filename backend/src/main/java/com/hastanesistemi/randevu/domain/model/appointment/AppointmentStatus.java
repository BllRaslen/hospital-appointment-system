package com.hastanesistemi.randevu.domain.model.appointment;

/**
 * Bir randevunun yasam dongusundeki (lifecycle) durumunu temsil eder.
 * Gereksinim: "Randevu icin onay mekanizmasi olacak."
 * <p>
 * Gecerli gecisler: BEKLEMEDE -> ONAYLANDI -> TAMAMLANDI
 *                    BEKLEMEDE veya ONAYLANDI -> IPTAL_EDILDI
 * Bu kurallar Appointment sinifindaki onayla()/iptalEt()/tamamla()
 * metotlarinda uygulanir (bkz. Appointment - KAPSULLEME).
 */
public enum AppointmentStatus {
    BEKLEMEDE,
    ONAYLANDI,
    IPTAL_EDILDI,
    TAMAMLANDI,
    GELMEDI
}
