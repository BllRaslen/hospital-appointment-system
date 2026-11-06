package com.hastanesistemi.randevu.domain.model.common;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Bir baslangic ve bitis zamani arasindaki araligi temsil eden degismez
 * (immutable) deger nesnesi. Randevularin cakisma kontrolu bu sinif
 * uzerinden yapilir.
 * <p>
 * OOP ilkesi: KAPSULLEME - ic durum (baslangic/bitis) disaridan degistirilemez;
 * tutarlilik kurali (baslangic < bitis) sadece constructor icinde uygulanir,
 * boylece gecersiz bir TimeSlot nesnesi olusturulamaz ("her zaman gecerli
 * nesne" ilkesi).
 */
public final class TimeSlot {

    private final LocalDateTime baslangic;
    private final LocalDateTime bitis;

    public TimeSlot(LocalDateTime baslangic, LocalDateTime bitis) {
        if (baslangic == null || bitis == null) {
            throw new IllegalArgumentException("Baslangic ve bitis zamanlari bos olamaz.");
        }
        if (!bitis.isAfter(baslangic)) {
            throw new IllegalArgumentException("Bitis zamani, baslangic zamanindan sonra olmalidir.");
        }
        this.baslangic = baslangic;
        this.bitis = bitis;
    }

    /**
     * Iki zaman araliginin birbiriyle cakisip cakismadigini kontrol eder.
     * Randevu olusturma akisinda ayni doktorun iki randevusu bu metotla
     * karsilastirilir.
     */
    public boolean cakisiyorMu(TimeSlot diger) {
        if (diger == null) {
            return false;
        }
        return this.baslangic.isBefore(diger.bitis) && diger.baslangic.isBefore(this.bitis);
    }

    public LocalDateTime getBaslangic() {
        return baslangic;
    }

    public LocalDateTime getBitis() {
        return bitis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot that = (TimeSlot) o;
        return Objects.equals(baslangic, that.baslangic) && Objects.equals(bitis, that.bitis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baslangic, bitis);
    }

    @Override
    public String toString() {
        return baslangic + " - " + bitis;
    }
}
