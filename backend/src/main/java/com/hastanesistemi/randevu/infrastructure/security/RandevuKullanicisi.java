package com.hastanesistemi.randevu.infrastructure.security;

import com.hastanesistemi.randevu.domain.model.person.Role;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.EmployeeEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.FacilityManagerEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.StaffEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * {@code PersonEntity} alt siniflarini (Patient/Doctor/Staff/FacilityManager)
 * Spring Security'nin {@code UserDetails} sozlesmesine BAGLAYAN (bridge) bir
 * ADAPTER sinifi.
 * <p>
 * OOP ilkesi: SOYUTLAMA + ADAPTER DESENI - domain/persistence katmanlarindaki
 * kisi hiyerarsisi (bkz. {@code PersonEntity}) ile Spring Security'nin kendi
 * sozlesmesi (UserDetails) birbirine UYUMSUZDUR; bu sinif araya girerek
 * ikisini birbirine CEVIRIR. Boylece {@code GuvenlikYapilandirmasi} ve
 * {@code JwtKimlikDogrulamaFiltresi}, hicbir zaman {@code PatientEntity} veya
 * {@code DoctorEntity} gibi somut persistence siniflarini DOGRUDAN GORMEZ.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - {@link #calisandanOlustur(EmployeeEntity)}
 * metodu, kendisine verilen {@code EmployeeEntity}'nin GERCEK (runtime)
 * alt turune ({@code instanceof}) bakarak dogru {@code Role} degerini secer;
 * {@code calisanlar} tablosu SINGLE_TABLE kalitimla polimorfik sorgulandigi
 * icin bu cozumleme burada yapilmak ZORUNDADIR.
 */
public final class RandevuKullanicisi implements UserDetails {

    private final Long kullaniciId;
    private final String email;
    private final String sifreHash;
    private final String adSoyad;
    private final Role rol;

    public RandevuKullanicisi(Long kullaniciId, String email, String sifreHash, String adSoyad, Role rol) {
        this.kullaniciId = kullaniciId;
        this.email = email;
        this.sifreHash = sifreHash;
        this.adSoyad = adSoyad;
        this.rol = rol;
    }

    public static RandevuKullanicisi hastadanOlustur(PatientEntity hasta) {
        return new RandevuKullanicisi(hasta.getId(), hasta.getEmail(), hasta.getSifreHash(),
                hasta.getAd() + " " + hasta.getSoyad(), Role.HASTA);
    }

    public static RandevuKullanicisi calisandanOlustur(EmployeeEntity calisan) {
        Role rol;
        if (calisan instanceof DoctorEntity) {
            rol = Role.DOKTOR;
        } else if (calisan instanceof StaffEntity) {
            rol = Role.SAGLIK_PERSONELI;
        } else if (calisan instanceof FacilityManagerEntity) {
            rol = Role.YONETICI;
        } else {
            throw new IllegalStateException("Bilinmeyen calisan turu: " + calisan.getClass());
        }
        return new RandevuKullanicisi(calisan.getId(), calisan.getEmail(), calisan.getSifreHash(),
                calisan.getAd() + " " + calisan.getSoyad(), rol);
    }

    public Long getKullaniciId() {
        return kullaniciId;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public Role getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return sifreHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
