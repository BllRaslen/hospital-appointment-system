package com.hastanesistemi.randevu.presentation.dto.hasta;

import com.hastanesistemi.randevu.domain.model.person.Patient;

import java.util.List;

/**
 * {@code Patient} domain nesnesinin, API disariya acarken kullandigi
 * salt-okunur gorunumu. Domain nesnesinin KENDISI disariya DOGRUDAN
 * donulmez - bu, Clean Architecture'in ic katmanlarinin dis dunyadan
 * (HTTP/JSON serilestirme detaylari) habersiz kalmasini saglar.
 */
public record HastaYanit(Long id, String ad, String soyad, String email, String telefonNo,
                          String kanGrubu, String sigortaNo, List<String> alerjiler) {

    public static HastaYanit fromDomain(Patient hasta) {
        return new HastaYanit(hasta.getId(), hasta.getAd(), hasta.getSoyad(), hasta.getEmail(),
                hasta.getTelefonNo(), hasta.getKanGrubu(), hasta.getSigortaNo(), hasta.getAlerjiler());
    }
}
