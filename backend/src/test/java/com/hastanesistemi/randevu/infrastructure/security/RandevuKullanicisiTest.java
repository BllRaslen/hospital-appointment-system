package com.hastanesistemi.randevu.infrastructure.security;

import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.FacilityManagerEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.StaffEntity;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code RandevuKullanicisi} adapter'inin, {@code calisanlar} tablosundaki
 * HER somut turden (Doctor/Staff/FacilityManager) dogru {@code Role} ve
 * yetkiyi (GrantedAuthority) uretip uretmedigini dogrular — bkz. sinifin
 * kendi Javadoc'u (cok biciminlilik/instanceof cozumlemesi).
 */
class RandevuKullanicisiTest {

    @Test
    void hastaIcinHastaYetkisiUretir() {
        PatientEntity hasta = yeniPersonEntity(new PatientEntity(), "ayse@example.com");

        RandevuKullanicisi kullanici = RandevuKullanicisi.hastadanOlustur(hasta);

        assertEquals("ayse@example.com", kullanici.getUsername());
        assertTrue(kullanici.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HASTA")));
    }

    @Test
    void doktorIcinDoktorYetkisiUretir() {
        DoctorEntity doktor = yeniCalisanEntity(new DoctorEntity(), "doktor@example.com");

        RandevuKullanicisi kullanici = RandevuKullanicisi.calisandanOlustur(doktor);

        assertTrue(kullanici.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOKTOR")));
    }

    @Test
    void saglikPersoneliIcinSaglikPersoneliYetkisiUretir() {
        StaffEntity personel = yeniCalisanEntity(new StaffEntity(), "personel@example.com");

        RandevuKullanicisi kullanici = RandevuKullanicisi.calisandanOlustur(personel);

        assertTrue(kullanici.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SAGLIK_PERSONELI")));
    }

    @Test
    void yoneticiIcinYoneticiYetkisiUretir() {
        FacilityManagerEntity yonetici = yeniCalisanEntity(new FacilityManagerEntity(), "yonetici@example.com");

        RandevuKullanicisi kullanici = RandevuKullanicisi.calisandanOlustur(yonetici);

        assertTrue(kullanici.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_YONETICI")));
    }

    private static <T extends PatientEntity> T yeniPersonEntity(T entity, String email) {
        entity.setId(1L);
        entity.setAd("Ayse");
        entity.setSoyad("Yilmaz");
        entity.setEmail(email);
        entity.setSifreHash("hash");
        return entity;
    }

    private static <T extends com.hastanesistemi.randevu.infrastructure.persistence.entity.EmployeeEntity> T
            yeniCalisanEntity(T entity, String email) {
        entity.setId(2L);
        entity.setAd("Mehmet");
        entity.setSoyad("Demir");
        entity.setEmail(email);
        entity.setSifreHash("hash");
        entity.setCalisanNo("C-001");
        entity.setIseBaslamaTarihi(LocalDate.of(2020, 1, 1));
        entity.setTemelMaas(BigDecimal.valueOf(20000));
        return entity;
    }
}
