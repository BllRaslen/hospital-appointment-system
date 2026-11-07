package com.hastanesistemi.randevu.application.pricing;

import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;

import java.math.BigDecimal;

/**
 * Bir hizmetin, belirli bir hasta icin NE KADARA mal olacagini hesaplama
 * algoritmasinin sozlesmesi.
 * <p>
 * OOP ilkesi: STRATEGY (STRATEJI) DESENI - {@code aylikEkOdemeHesapla()}
 * (Employee hiyerarsisinde) KALITIM ile cok biciminlik sagliyordu: her
 * calisan TURU sabit bir hesaplama koduna baglidir. Burada ise ayni
 * hesaplama turunun (ucretlendirme) BIRDEN FAZLA algoritmasi, KALITIM
 * KULLANMADAN, calisma zamaninda (runtime) DEGISTIRILEBILIR sekilde
 * KOMPOZISYON ile enjekte edilir (ornegin bir randevu servisine hangi
 * stratejinin kullanilacagi disaridan verilir). Bu, "kalitim yerine
 * kompozisyonu tercih et" ilkesinin klasik bir ornegidir.
 */
public interface UcretlendirmeStrategy {

    BigDecimal hesapla(MedicalService hizmet, Patient hasta);
}
