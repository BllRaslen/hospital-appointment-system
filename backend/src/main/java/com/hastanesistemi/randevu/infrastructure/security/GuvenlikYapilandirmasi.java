package com.hastanesistemi.randevu.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security'nin merkezi yapilandirmasi: sifre kodlama (hashing),
 * kimlik dogrulama saglayicisi ve JWT tabanli, OTURUMSUZ (stateless) filtre
 * zinciri burada tanimlanir.
 * <p>
 * OOP ilkesi: KOMPOZISYON - bu sinif, kendi basina kimlik dogrulama MANTIGI
 * ICERMEZ; bunun yerine {@code KullaniciDetayServisi} (UserDetailsService),
 * {@code PasswordEncoder} ve {@code JwtKimlikDogrulamaFiltresi} nesnelerini
 * BIR ARAYA GETIRIR (has-a iliskisi) — tipki {@code RandevuServisi}'nin
 * application katmaninda {@code UcretlendirmeStrategy}'yi enjekte edilen bir
 * PARCA olarak kullanmasi gibi (bkz. docs/OOP-PRENSIPLERI.md bolum 7).
 * <p>
 * API STATELESS'TIR (sunucu oturum/session SAKLAMAZ) — her istek, kendi
 * kimligini {@code Authorization} basligindaki JWT ile kanitlar; bu yuzden
 * CSRF korumasi (cerez tabanli oturumlar icin anlamlidir) devre disi
 * birakilir ve session olusturma politikasi {@code STATELESS} olarak
 * ayarlanir. {@code /api/auth/**} (giris/kayit - bkz. {@code AuthController})
 * herkese ACIKTIR; diger TUM endpoint'ler gecerli bir JWT gerektirir. CORS
 * (bkz. {@code CorsYapilandirmasi}), Next.js frontend'in farkli bir
 * origin'den (localhost:3000) istek atabilmesi icin acilir.
 */
@Configuration
@EnableMethodSecurity
public class GuvenlikYapilandirmasi {

    private final JwtKimlikDogrulamaFiltresi jwtKimlikDogrulamaFiltresi;
    private final KullaniciDetayServisi kullaniciDetayServisi;

    public GuvenlikYapilandirmasi(JwtKimlikDogrulamaFiltresi jwtKimlikDogrulamaFiltresi,
                                   KullaniciDetayServisi kullaniciDetayServisi) {
        this.jwtKimlikDogrulamaFiltresi = jwtKimlikDogrulamaFiltresi;
        this.kullaniciDetayServisi = kullaniciDetayServisi;
    }

    @Bean
    public PasswordEncoder sifreKodlayici() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider kimlikDogrulamaSaglayici() {
        DaoAuthenticationProvider saglayici = new DaoAuthenticationProvider();
        saglayici.setUserDetailsService(kullaniciDetayServisi);
        saglayici.setPasswordEncoder(sifreKodlayici());
        return saglayici;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration yapilandirma) throws Exception {
        return yapilandirma.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain guvenlikFiltreZinciri(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> { })
                .sessionManagement(oturum -> oturum.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(istekler -> istekler
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(kimlikDogrulamaSaglayici())
                .addFilterBefore(jwtKimlikDogrulamaFiltresi, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
