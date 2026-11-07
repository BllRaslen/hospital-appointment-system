package com.hastanesistemi.randevu.application.factory;

import com.hastanesistemi.randevu.domain.model.person.Specialty;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * {@link EmployeeFactory} tarafindan kullanilan, calisan olusturmak icin
 * gerekli tum alanlari tasiyan degismez (immutable) veri tasiyici.
 * <p>
 * {@code uzmanlikAlani}/{@code diplomaNo} sadece DOKTOR olusturulurken,
 * {@code unvan} sadece SAGLIK_PERSONELI olusturulurken kullanilir; digerleri
 * ilgisiz oldugunda null birakilabilir. Boylece tek bir fabrika metodu, farkli
 * calisan turlerine ozgu alanlari tek bir parametre listesi ile tasiyabilir.
 */
public record EmployeeOlusturmaBilgisi(
        Long id,
        String ad,
        String soyad,
        String tcKimlikNo,
        String email,
        String telefonNo,
        LocalDate dogumTarihi,
        String calisanNo,
        LocalDate iseBaslamaTarihi,
        BigDecimal temelMaas,
        Specialty uzmanlikAlani,
        String diplomaNo,
        String unvan) {
}
