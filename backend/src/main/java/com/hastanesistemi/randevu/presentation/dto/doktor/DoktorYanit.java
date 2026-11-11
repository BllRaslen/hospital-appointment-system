package com.hastanesistemi.randevu.presentation.dto.doktor;

import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Specialty;

/** {@code Doctor} domain nesnesinin API gorunumu. Bkz. {@code HastaYanit}. */
public record DoktorYanit(Long id, String ad, String soyad, String email, Specialty uzmanlikAlani,
                           String diplomaNo, int tamamlananRandevuSayisi) {

    public static DoktorYanit fromDomain(Doctor doktor) {
        return new DoktorYanit(doktor.getId(), doktor.getAd(), doktor.getSoyad(), doktor.getEmail(),
                doktor.getUzmanlikAlani(), doktor.getDiplomaNo(), doktor.getTamamlananRandevuSayisi());
    }
}
