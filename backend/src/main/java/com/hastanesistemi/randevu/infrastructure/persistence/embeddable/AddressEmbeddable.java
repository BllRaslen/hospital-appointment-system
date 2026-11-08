package com.hastanesistemi.randevu.infrastructure.persistence.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * {@code Address} deger nesnesinin (domain/model/common) JPA karsiligi.
 * <p>
 * Domain katmani JPA'ya bagimli OLMAMALIDIR (Clean Architecture'in Bagimlilik
 * Kurali); bu yuzden {@code Address} sinifinin kendisine hicbir {@code @Entity}/
 * {@code @Embeddable} anotasyonu eklenmez. Bunun yerine, SADECE bu
 * infrastructure katmaninda, ayni alanlari tasiyan paralel bir "gomulebilir"
 * (embeddable) sinif tanimlanir; domain <-> entity donusumu mapper siniflari
 * tarafindan yapilir.
 */
@Embeddable
public class AddressEmbeddable {

    @Column(name = "sehir")
    private String sehir;

    @Column(name = "ilce")
    private String ilce;

    @Column(name = "acik_adres")
    private String acikAdres;

    @Column(name = "posta_kodu")
    private String postaKodu;

    protected AddressEmbeddable() {
        // JPA icin zorunlu bos constructor.
    }

    public AddressEmbeddable(String sehir, String ilce, String acikAdres, String postaKodu) {
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
}
