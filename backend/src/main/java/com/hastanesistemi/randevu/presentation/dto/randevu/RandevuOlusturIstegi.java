package com.hastanesistemi.randevu.presentation.dto.randevu;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Yeni randevu olusturma istegi. Hasta kimligi (hastaId) KASITLI OLARAK bu
 * govdede YOKTUR - oturum acmis hastanin kimligi, JWT'den (SecurityContext /
 * {@code RandevuKullanicisi}) cozumlenir; boylece bir hasta BASKA bir hasta
 * adina randevu olusturamaz (bkz. {@code RandevuController.randevuOlustur}).
 */
public record RandevuOlusturIstegi(
        @NotNull(message = "Doktor secilmelidir.") Long doktorId,
        @NotNull(message = "Hizmet secilmelidir.") Long hizmetId,
        @NotNull(message = "Tesis secilmelidir.") Long tesisId,
        @NotNull(message = "Baslangic zamani bos olamaz.") @Future(message = "Baslangic zamani gelecekte olmalidir.") LocalDateTime baslangic,
        @NotNull(message = "Bitis zamani bos olamaz.") @Future(message = "Bitis zamani gelecekte olmalidir.") LocalDateTime bitis) {
}
