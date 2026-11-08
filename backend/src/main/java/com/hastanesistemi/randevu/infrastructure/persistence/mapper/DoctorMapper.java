package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.facility.WorkingHours;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Specialty;
import com.hastanesistemi.randevu.infrastructure.persistence.embeddable.WorkingHoursEmbeddable;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Doctor} (domain) ile {@code DoctorEntity} (JPA) arasindaki donusumu
 * yapan, durumsuz yardimci sinif. Bkz. {@link PatientMapper} ic
 * dokumantasyonu (mapper'in amaci).
 * <p>
 * {@code sunduguHizmetler}, Doctor'dan BASKA bir aggregate'e (MedicalService)
 * cross-aggregate referanstir. {@code toEntity} bu yuzden hazir
 * ("getReferenceById" ile cozumlenmis, GERCEKTEN VAR OLAN) entity listesini
 * PARAMETRE olarak alir — mapper kendisi yeni bir {@code MedicalServiceEntity}
 * INSA ETMEZ. Aksi halde Hibernate, zaten var olan bir hizmeti TEKRAR
 * eklemeye calisir ve "gecici (transient) nesne" hatasi verirdi. Bu
 * cozumlemeyi yapmak (hangi hizmetlerin GERCEKTEN var oldugunu bulmak)
 * adapter'in sorumlulugundadir, cunku sadece adapter'in ilgili
 * repository'ye erisimi vardir.
 */
public final class DoctorMapper {

    private DoctorMapper() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static DoctorEntity toEntity(Doctor doktor, List<MedicalServiceEntity> sunduguHizmetlerReferanslari) {
        DoctorEntity entity = new DoctorEntity();
        entity.setId(doktor.getId());
        entity.setAd(doktor.getAd());
        entity.setSoyad(doktor.getSoyad());
        entity.setTcKimlikNo(doktor.getTcKimlikNo());
        entity.setEmail(doktor.getEmail());
        entity.setTelefonNo(doktor.getTelefonNo());
        entity.setDogumTarihi(doktor.getDogumTarihi());
        entity.setCalisanNo(doktor.getCalisanNo());
        entity.setIseBaslamaTarihi(doktor.getIseBaslamaTarihi());
        entity.setTemelMaas(doktor.getTemelMaas());
        entity.setUzmanlikAlani(doktor.getUzmanlikAlani() != null ? doktor.getUzmanlikAlani().name() : null);
        entity.setDiplomaNo(doktor.getDiplomaNo());
        entity.setTamamlananRandevuSayisi(doktor.getTamamlananRandevuSayisi());
        entity.setSunduguHizmetler(sunduguHizmetlerReferanslari);

        List<WorkingHoursEmbeddable> uygunluklar = new ArrayList<>();
        for (WorkingHours uygunluk : doktor.getUygunlukZamanlari()) {
            uygunluklar.add(new WorkingHoursEmbeddable(uygunluk.getGun(), uygunluk.getBaslangicSaati(), uygunluk.getBitisSaati()));
        }
        entity.setUygunlukZamanlari(uygunluklar);
        return entity;
    }

    public static Doctor toDomain(DoctorEntity entity) {
        Specialty uzmanlik = entity.getUzmanlikAlani() != null ? Specialty.valueOf(entity.getUzmanlikAlani()) : null;
        Doctor doktor = new Doctor(entity.getId(), entity.getAd(), entity.getSoyad(), entity.getTcKimlikNo(),
                entity.getEmail(), entity.getTelefonNo(), entity.getDogumTarihi(), entity.getCalisanNo(),
                entity.getIseBaslamaTarihi(), entity.getTemelMaas(), uzmanlik, entity.getDiplomaNo());
        doktor.tamamlananRandevuSayisiniAyarla(entity.getTamamlananRandevuSayisi());

        for (MedicalServiceEntity hizmetEntity : entity.getSunduguHizmetler()) {
            doktor.hizmetEkle(MedicalServiceMapper.toDomain(hizmetEntity));
        }
        for (WorkingHoursEmbeddable uygunlukEntity : entity.getUygunlukZamanlari()) {
            doktor.uygunlukEkle(new WorkingHours(uygunlukEntity.getGun(), uygunlukEntity.getBaslangicSaati(), uygunlukEntity.getBitisSaati()));
        }
        return doktor;
    }
}
