package com.hastanesistemi.randevu.domain.model.common;

import java.util.Objects;

/**
 * Adres bilgisini temsil eden DEGISMEZ (immutable) bir deger nesnesi (value object).
 * <p>
 * OOP ilkesi: KAPSULLEME (encapsulation) - tum alanlar 'private final' olarak
 * tanimlanmistir, hicbir setter metodu yoktur. Bir Address nesnesi olusturulduktan
 * sonra icerigi asla degistirilemez; adres degismesi gerektiginde yeni bir
 * Address nesnesi olusturulur. Bu, deger nesneleri icin tercih edilen bir
 * tasarimdir ve yan etkisiz (side-effect free), thread-safe kod saglar.
 */
public final class Address {

    private final String sehir;
    private final String ilce;
    private final String acikAdres;
    private final String postaKodu;

    public Address(String sehir, String ilce, String acikAdres, String postaKodu) {
        if (sehir == null || sehir.isBlank()) {
            throw new IllegalArgumentException("Sehir alani bos olamaz.");
        }
        if (acikAdres == null || acikAdres.isBlank()) {
            throw new IllegalArgumentException("Acik adres alani bos olamaz.");
        }
        this.sehir = sehir;
        this.ilce = ilce;
        this.acikAdres = acikAdres;
        this.postaKodu = postaKodu;
    }

    public String getSehir() {
        return sehir;
    }

    public String getIlce() {
        return ilce;
    }

    public String getAcikAdres() {
        return acikAdres;
    }

    public String getPostaKodu() {
        return postaKodu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address that = (Address) o;
        return Objects.equals(sehir, that.sehir)
                && Objects.equals(ilce, that.ilce)
                && Objects.equals(acikAdres, that.acikAdres)
                && Objects.equals(postaKodu, that.postaKodu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sehir, ilce, acikAdres, postaKodu);
    }

    @Override
    public String toString() {
        return acikAdres + ", " + ilce + " / " + sehir + (postaKodu != null ? " " + postaKodu : "");
    }
}
