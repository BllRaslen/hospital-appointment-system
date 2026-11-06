package com.hastanesistemi.randevu.domain.model.notification;

/**
 * Sisteme kayitli ve bildirim alabilen varliklarin (Hasta, Doktor) uygulamasi
 * gereken sozlesme (contract).
 * <p>
 * OOP ilkesi: COK BICIMLILIK (polymorphism) - ARAYUZ (interface) uzerinden.
 * Person sinif hiyerarsisindeki kalitima dayali cok biciminliligin yaninda,
 * burada farkli sinif hiyerarsilerinden (Patient ve Doctor, her ikisi de
 * Person'dan tureniyor olsa da) nesnelerin ortak bir davranisi AYNI ARAYUZ
 * uzerinden farkli sekillerde uygulamasini gosteriyoruz:
 * <ul>
 *   <li>Patient.bildirimGonder(...) -> e-posta ile bildirim simulasyonu</li>
 *   <li>Doctor.bildirimGonder(...)  -> SMS ile bildirim simulasyonu</li>
 * </ul>
 * Cagiran kod (ornegin randevu onay servisi) sadece "Notifiable" tipini bilir;
 * hangi somut sinifin calisacagi calisma zamaninda (runtime) belirlenir.
 */
public interface Notifiable {

    /**
     * Ilgili kisiye verilen mesaji kendi kanali uzerinden "gonderir" ve
     * gonderim sonucunu aciklayan bir metin dondurur.
     */
    String bildirimGonder(String mesaj);
}
