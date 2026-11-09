package com.hastanesistemi.randevu.infrastructure.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Her HTTP istegine (request) BIR KEZ uygulanan, {@code Authorization:
 * Bearer &lt;token&gt;} basligindaki JWT'yi okuyup gecerliyse Spring
 * Security'nin {@code SecurityContext}'ine kimligi yerlestiren filtre.
 * <p>
 * OOP ilkesi: SABLON METOT (Template Method) - {@code OncePerRequestFilter}
 * (Spring Security'nin kendi sinifi), "filtre zincirinde bir kez calis"
 * iskeletini saglar; bu sinif sadece DEGISKEN parcayi
 * ({@code doFilterInternal}) uygular. Ayni kalitim/polimorfizm ilkesi,
 * domain katmanindaki {@code Employee.toplamAylikOdemeHesapla()} ornegiyle
 * birebir ayni mantigi tasir (bkz. docs/OOP-PRENSIPLERI.md bolum 3).
 * <p>
 * Token GECERSIZSE (yok, bozuk, suresi dolmus) bu filtre HATA FIRLATMAZ;
 * sadece kimligi SecurityContext'e YERLESTIRMEZ ve istegi oldugu gibi bir
 * sonraki filtreye devreder — nihai 401/403 kararini
 * {@code GuvenlikYapilandirmasi}'ndaki yetkilendirme kurallari verir.
 */
@Component
public class JwtKimlikDogrulamaFiltresi extends OncePerRequestFilter {

    private static final String YETKI_BASLIGI = "Authorization";
    private static final String TOKEN_ON_EKI = "Bearer ";

    private final JwtServisi jwtServisi;
    private final KullaniciDetayServisi kullaniciDetayServisi;

    public JwtKimlikDogrulamaFiltresi(JwtServisi jwtServisi, KullaniciDetayServisi kullaniciDetayServisi) {
        this.jwtServisi = jwtServisi;
        this.kullaniciDetayServisi = kullaniciDetayServisi;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String yetkiBasligi = request.getHeader(YETKI_BASLIGI);
        if (yetkiBasligi == null || !yetkiBasligi.startsWith(TOKEN_ON_EKI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = yetkiBasligi.substring(TOKEN_ON_EKI.length());
        try {
            String eposta = jwtServisi.kullaniciAdiniCikar(token);
            boolean kimlikZatenKurulu = SecurityContextHolder.getContext().getAuthentication() != null;
            if (eposta != null && !kimlikZatenKurulu) {
                UserDetails kullanici = kullaniciDetayServisi.loadUserByUsername(eposta);
                if (jwtServisi.tokenGecerliMi(token, kullanici)) {
                    UsernamePasswordAuthenticationToken kimlik =
                            new UsernamePasswordAuthenticationToken(kullanici, null, kullanici.getAuthorities());
                    kimlik.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(kimlik);
                }
            }
        } catch (JwtException | IllegalArgumentException hata) {
            // Bozuk/sahte token: kimlik kurulmaz, istek "anonim" olarak devam eder.
        }

        filterChain.doFilter(request, response);
    }
}
