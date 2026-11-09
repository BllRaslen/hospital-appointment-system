package com.hastanesistemi.randevu.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * JWT (JSON Web Token) uretimi ve dogrulanmasini KAPSULLEYEN servis.
 * <p>
 * OOP ilkesi: KAPSULLEME - gizli anahtar ({@code anahtar}) ve token'in dahili
 * ayrıstirma (parsing) detaylari (jjwt kutuphanesinin API'si) tamamen
 * {@code private}'tir; disariya sadece "token uret", "kullanici adini cikar"
 * ve "token gecerli mi" gibi ANLAMLI, yuksek seviye islemler acilir. Cagiran
 * kod (filtre/controller) jjwt kutuphanesinin var oldugunu bile bilmek
 * zorunda degildir.
 */
@Service
public class JwtServisi {

    private final SecretKey anahtar;
    private final long gecerlilikSuresiMs;

    public JwtServisi(@Value("${jwt.secret}") String gizliAnahtar,
                       @Value("${jwt.expiration-ms:3600000}") long gecerlilikSuresiMs) {
        this.anahtar = Keys.hmacShaKeyFor(gizliAnahtar.getBytes(StandardCharsets.UTF_8));
        this.gecerlilikSuresiMs = gecerlilikSuresiMs;
    }

    public String tokenUret(UserDetails kullanici) {
        Instant simdi = Instant.now();
        List<String> yetkiler = kullanici.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(kullanici.getUsername())
                .claim("yetkiler", yetkiler)
                .issuedAt(Date.from(simdi))
                .expiration(Date.from(simdi.plusMillis(gecerlilikSuresiMs)))
                .signWith(anahtar)
                .compact();
    }

    public String kullaniciAdiniCikar(String token) {
        return claimleriCoz(token).getSubject();
    }

    /**
     * Token'in IMZASININ gecerli oldugunu VE suresinin dolmadigini VE istenen
     * kullaniciya ait oldugunu dogrular. Herhangi bir sorunda (bozuk/sahte
     * token, suresi dolmus token) exception firlatmak yerine sade {@code
     * false} doner — bu, cagiran filtrenin (bkz. {@code
     * JwtKimlikDogrulamaFiltresi}) her istekte try/catch yazmasini onler.
     */
    public boolean tokenGecerliMi(String token, UserDetails kullanici) {
        try {
            String tokendekiEposta = kullaniciAdiniCikar(token);
            return tokendekiEposta.equals(kullanici.getUsername()) && !suresiDolmusMu(token);
        } catch (JwtException | IllegalArgumentException hata) {
            return false;
        }
    }

    private boolean suresiDolmusMu(String token) {
        return claimleriCoz(token).getExpiration().before(new Date());
    }

    private Claims claimleriCoz(String token) {
        return Jwts.parser()
                .verifyWith(anahtar)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
