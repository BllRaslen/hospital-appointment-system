package com.hastanesistemi.randevu.infrastructure.security;

import com.hastanesistemi.randevu.domain.model.person.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code JwtServisi}'nin token uretimi/dogrulanmasini, Spring/ApplicationContext
 * olmadan (servis dogrudan constructor ile ornekleniyor), saf Java ile test
 * eder — bkz. {@code DoctorMapperTest} ic dokumantasyonu (hizli testlerin
 * nedeni).
 */
class JwtServisiTest {

    private static final String TEST_GIZLI_ANAHTARI =
            "test-ortaminda-kullanilan-en-az-32-byte-uzunlugunda-gizli-anahtar";

    private final JwtServisi jwtServisi = new JwtServisi(TEST_GIZLI_ANAHTARI, 60_000L);

    @Test
    void uretilenTokenKendiKullanicisiIcinGecerlidir() {
        UserDetails kullanici = new RandevuKullanicisi(1L, "ayse@example.com", "hash", "Ayse Yilmaz", Role.HASTA);

        String token = jwtServisi.tokenUret(kullanici);

        assertEquals("ayse@example.com", jwtServisi.kullaniciAdiniCikar(token));
        assertTrue(jwtServisi.tokenGecerliMi(token, kullanici));
    }

    @Test
    void baskaKullanicininTokeniKendisiIcinGecersizdir() {
        UserDetails hasta = new RandevuKullanicisi(1L, "hasta@example.com", "hash", "Hasta", Role.HASTA);
        UserDetails doktor = new RandevuKullanicisi(2L, "doktor@example.com", "hash", "Doktor", Role.DOKTOR);

        String hastaTokeni = jwtServisi.tokenUret(hasta);

        assertFalse(jwtServisi.tokenGecerliMi(hastaTokeni, doktor));
    }

    @Test
    void suresiDolmusTokenGecersizdir() throws InterruptedException {
        JwtServisi kisaSureliServis = new JwtServisi(TEST_GIZLI_ANAHTARI, 1L);
        UserDetails kullanici = new RandevuKullanicisi(1L, "ayse@example.com", "hash", "Ayse Yilmaz", Role.HASTA);

        String token = kisaSureliServis.tokenUret(kullanici);
        Thread.sleep(10);

        assertFalse(kisaSureliServis.tokenGecerliMi(token, kullanici));
    }

    @Test
    void bozukTokenGecersizdir() {
        UserDetails kullanici = new RandevuKullanicisi(1L, "ayse@example.com", "hash", "Ayse Yilmaz", Role.HASTA);

        assertFalse(jwtServisi.tokenGecerliMi("bu-gecerli-bir-jwt-degil", kullanici));
    }
}
