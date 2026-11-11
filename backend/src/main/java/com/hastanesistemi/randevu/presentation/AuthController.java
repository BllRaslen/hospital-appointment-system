package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.factory.EmployeeFactory;
import com.hastanesistemi.randevu.application.factory.EmployeeOlusturmaBilgisi;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Role;
import com.hastanesistemi.randevu.infrastructure.security.JwtServisi;
import com.hastanesistemi.randevu.infrastructure.security.KayitServisi;
import com.hastanesistemi.randevu.infrastructure.security.RandevuKullanicisi;
import com.hastanesistemi.randevu.presentation.dto.auth.DoktorKayitIstegi;
import com.hastanesistemi.randevu.presentation.dto.auth.GirisIstegi;
import com.hastanesistemi.randevu.presentation.dto.auth.GirisYaniti;
import com.hastanesistemi.randevu.presentation.dto.auth.HastaKayitIstegi;
import com.hastanesistemi.randevu.presentation.dto.doktor.DoktorYanit;
import com.hastanesistemi.randevu.presentation.dto.hasta.HastaYanit;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kimlik dogrulama (giris) ve kayit endpoint'lerini acar. {@code
 * /api/auth/**}, {@code GuvenlikYapilandirmasi}'nda HERKESE ACIK olarak
 * tanimlanmistir - henuz kimligi olmayan bir kullanici giris/kayit
 * yapabilmelidir.
 * <p>
 * OOP ilkesi: FACTORY METHOD DESENI - {@code doktorKayitOl}, hangi somut
 * {@code Employee} alt sinifinin ({@code Doctor}) olusturulacagini KENDISI
 * BILMEZ; bu karar {@code EmployeeFactory}'ye delege edilir (bkz. {@code
 * EmployeeFactory} ic dokumantasyonu).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtServisi jwtServisi;
    private final KayitServisi kayitServisi;

    public AuthController(AuthenticationManager authenticationManager, JwtServisi jwtServisi,
                           KayitServisi kayitServisi) {
        this.authenticationManager = authenticationManager;
        this.jwtServisi = jwtServisi;
        this.kayitServisi = kayitServisi;
    }

    /** Herhangi bir rol (hasta/doktor/personel/yonetici) ayni endpoint ile giris yapar. */
    @PostMapping("/giris")
    public GirisYaniti girisYap(@Valid @RequestBody GirisIstegi istek) {
        Authentication kimlik = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(istek.email(), istek.sifre()));
        RandevuKullanicisi kullanici = (RandevuKullanicisi) kimlik.getPrincipal();
        String token = jwtServisi.tokenUret(kullanici);
        return new GirisYaniti(token, kullanici.getKullaniciId(), kullanici.getAdSoyad(), kullanici.getRol());
    }

    /** Hastanin kendi kendine kayit (self sign-up) akisi. Kimlik dogrulama GEREKMEZ. */
    @PostMapping("/kayit")
    public ResponseEntity<HastaYanit> hastaKayitOl(@Valid @RequestBody HastaKayitIstegi istek) {
        Patient hasta = new Patient(null, istek.ad(), istek.soyad(), istek.tcKimlikNo(), istek.email(),
                istek.telefonNo(), istek.dogumTarihi(), istek.kanGrubu(), istek.sigortaNo());
        Patient kaydedilen = kayitServisi.hastaKaydet(hasta, istek.sifre());
        return ResponseEntity.status(HttpStatus.CREATED).body(HastaYanit.fromDomain(kaydedilen));
    }

    /** Bir yoneticinin yeni bir doktor hesabi acmasi akisi. SADECE ROLE_YONETICI cagirabilir. */
    @PostMapping("/kayit-doktor")
    @PreAuthorize("hasRole('YONETICI')")
    public ResponseEntity<DoktorYanit> doktorKayitOl(@Valid @RequestBody DoktorKayitIstegi istek) {
        EmployeeOlusturmaBilgisi bilgi = new EmployeeOlusturmaBilgisi(null, istek.ad(), istek.soyad(),
                istek.tcKimlikNo(), istek.email(), istek.telefonNo(), istek.dogumTarihi(), istek.calisanNo(),
                istek.iseBaslamaTarihi(), istek.temelMaas(), istek.uzmanlikAlani(), istek.diplomaNo(), null);
        Doctor doktor = (Doctor) EmployeeFactory.olustur(Role.DOKTOR, bilgi);
        Doctor kaydedilen = kayitServisi.doktorKaydet(doktor, istek.sifre());
        return ResponseEntity.status(HttpStatus.CREATED).body(DoktorYanit.fromDomain(kaydedilen));
    }
}
