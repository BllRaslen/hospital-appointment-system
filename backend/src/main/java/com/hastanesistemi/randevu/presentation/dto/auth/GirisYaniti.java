package com.hastanesistemi.randevu.presentation.dto.auth;

import com.hastanesistemi.randevu.domain.model.person.Role;

/**
 * Basarili giris sonrasi doneni JWT ve temel kullanici bilgisi. Frontend,
 * bu token'i sonraki tum isteklerde {@code Authorization: Bearer <token>}
 * basligiyla gonderir.
 */
public record GirisYaniti(String token, Long kullaniciId, String adSoyad, Role rol) {
}
