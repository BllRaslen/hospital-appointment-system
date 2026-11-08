package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.common.Address;
import com.hastanesistemi.randevu.domain.model.facility.Clinic;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.facility.Hospital;
import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.WorkingHoursEmbeddable;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.ClinicEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HospitalEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code HealthcareFacility} (domain, Hospital/Clinic) ile {@code
 * HealthcareFacilityEntity} (JPA) arasindaki donusumu yapan, durumsuz
 * yardimci sinif.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - {@code toEntity}/{@code toDomain}, tesisin
 * GERCEK (runtime) turune ({@code instanceof Hospital}/{@code Clinic} ya da
 * {@code instanceof HospitalEntity}/{@code ClinicEntity}) gore dogru somut
 * sinifi insa eder; cagiran kod (adapter) hangi somut turle calistigini
 * bilmek zorunda degildir.
 * <p>
 * {@code sunduguHizmetler} icin bkz. {@link DoctorMapper} ic dokumantasyonu
 * (cross-aggregate referans cozumleme sorumlulugu adapter'dadir).
 */
public final class HealthcareFacilityMapper {

    private HealthcareFacilityMapper() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static HealthcareFacilityEntity toEntity(HealthcareFacility tesis,
                                                      List<MedicalServiceEntity> sunduguHizmetlerReferanslari) {
        HealthcareFacilityEntity entity;
        if (tesis instanceof Hospital hastane) {
            HospitalEntity hospitalEntity = new HospitalEntity();
            hospitalEntity.setYatakSayisi(hastane.getYatakSayisi());
            hospitalEntity.setAcilServisVarMi(hastane.isAcilServisVarMi());
            entity = hospitalEntity;
        } else if (tesis instanceof Clinic klinik) {
            ClinicEntity clinicEntity = new ClinicEntity();
            clinicEntity.setMuayeneOdasiSayisi(klinik.getMuayeneOdasiSayisi());
            entity = clinicEntity;
        } else {
            throw new IllegalArgumentException("Desteklenmeyen tesis turu: " + tesis.getClass());
        }

        entity.setId(tesis.getId());
        entity.setAd(tesis.getAd());
        entity.setVergiNo(tesis.getVergiNo());
        if (tesis.getAdres() != null) {
            Address adres = tesis.getAdres();
            entity.setAdres(new AddressEmbeddable(adres.getSehir(), adres.getIlce(), adres.getAcikAdres(), adres.getPostaKodu()));
        }

        List<WorkingHoursEmbeddable> saatler = new ArrayList<>();
        for (WorkingHours saat : tesis.getCalismaSaatleri()) {
            saatler.add(new WorkingHoursEmbeddable(saat.getGun(), saat.getBaslangicSaati(), saat.getBitisSaati()));
        }
        entity.setCalismaSaatleri(saatler);
        entity.setSunduguHizmetler(sunduguHizmetlerReferanslari);
        return entity;
    }

    public static HealthcareFacility toDomain(HealthcareFacilityEntity entity) {
        AddressEmbeddable adresEntity = entity.getAdres();
        Address adres = adresEntity != null
                ? new Address(adresEntity.getSehir(), adresEntity.getIlce(), adresEntity.getAcikAdres(), adresEntity.getPostaKodu())
                : null;

        HealthcareFacility tesis;
        if (entity instanceof HospitalEntity hastaneEntity) {
            tesis = new Hospital(entity.getId(), entity.getAd(), adres, entity.getVergiNo(),
                    hastaneEntity.getYatakSayisi(), hastaneEntity.isAcilServisVarMi());
        } else if (entity instanceof ClinicEntity klinikEntity) {
            tesis = new Clinic(entity.getId(), entity.getAd(), adres, entity.getVergiNo(), klinikEntity.getMuayeneOdasiSayisi());
        } else {
            throw new IllegalArgumentException("Desteklenmeyen tesis entity turu: " + entity.getClass());
        }

        for (WorkingHoursEmbeddable saat : entity.getCalismaSaatleri()) {
            tesis.calismaSaatiEkle(new WorkingHours(saat.getGun(), saat.getBaslangicSaati(), saat.getBitisSaati()));
        }
        for (MedicalServiceEntity hizmetEntity : entity.getSunduguHizmetler()) {
            tesis.hizmetEkle(MedicalServiceMapper.toDomain(hizmetEntity));
        }
        return tesis;
    }
}
