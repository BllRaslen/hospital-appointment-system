package com.hastanesistemi.randevu.infrastructure.persistence.mapper;

import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.appointment.AppointmentStatus;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.AppointmentEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.DoctorEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.HealthcareFacilityEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.MedicalServiceEntity;
import com.hastanesistemi.randevu.infrastructure.persistence.entity.PatientEntity;

/**
 * {@code Appointment} (domain) ile {@code AppointmentEntity} (JPA) arasindaki
 * donusumu yapan, durumsuz yardimci sinif.
 * <p>
 * {@code hasta}/{@code doktor}/{@code hizmet}/{@code tesis} DORDU DE
 * cross-aggregate referanstir (bkz. {@link DoctorMapper} ic dokumantasyonu).
 * Bu yuzden {@code toEntity}, bunlarin HAZIR COZUMLENMIS entity karsiliklarini
 * parametre olarak alir — adapter bunlari ilgili *JpaRepository'lerden
 * {@code getReferenceById(id)} ile (ekstra bir SELECT sorgusu olmadan,
 * sadece "bu id'ye sahip bir kayit zaten var" varsayimiyla) elde eder.
 * {@code toDomain} yonunde ise sorun yoktur: okunan entity'ler zaten
 * veritabanindan TAM olarak yuklenmistir, dogrudan ilgili mapper'larla
 * domain nesnesine cevrilebilirler.
 */
public final class AppointmentMapper {

    private AppointmentMapper() {
        // Yardimci (utility) sinif; ornegi olusturulamaz.
    }

    public static AppointmentEntity toEntity(Appointment randevu, PatientEntity hastaReferansi,
                                              DoctorEntity doktorReferansi, MedicalServiceEntity hizmetReferansi,
                                              HealthcareFacilityEntity tesisReferansi) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(randevu.getId());
        entity.setHasta(hastaReferansi);
        entity.setDoktor(doktorReferansi);
        entity.setHizmet(hizmetReferansi);
        entity.setTesis(tesisReferansi);
        entity.setBaslangicZamani(randevu.getZamanAraligi().getBaslangic());
        entity.setBitisZamani(randevu.getZamanAraligi().getBitis());
        entity.setDurum(randevu.getDurum().name());
        entity.setOlusturmaZamani(randevu.getOlusturmaZamani());
        entity.setOnayZamani(randevu.getOnayZamani());
        entity.setNotlar(randevu.getNotlar());
        return entity;
    }

    public static Appointment toDomain(AppointmentEntity entity) {
        Patient hasta = PatientMapper.toDomain(entity.getHasta());
        Doctor doktor = DoctorMapper.toDomain(entity.getDoktor());
        MedicalService hizmet = MedicalServiceMapper.toDomain(entity.getHizmet());
        HealthcareFacility tesis = HealthcareFacilityMapper.toDomain(entity.getTesis());
        TimeSlot zamanAraligi = new TimeSlot(entity.getBaslangicZamani(), entity.getBitisZamani());
        AppointmentStatus durum = AppointmentStatus.valueOf(entity.getDurum());

        return Appointment.depodanYenidenOlustur(entity.getId(), hasta, doktor, hizmet, tesis, zamanAraligi,
                durum, entity.getOlusturmaZamani(), entity.getOnayZamani(), entity.getNotlar());
    }
}
