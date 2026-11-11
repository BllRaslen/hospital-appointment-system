package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.application.port.out.AppointmentRepository;
import com.hastanesistemi.randevu.application.port.out.DoctorRepository;
import com.hastanesistemi.randevu.application.port.out.HealthcareFacilityRepository;
import com.hastanesistemi.randevu.application.port.out.MedicalServiceRepository;
import com.hastanesistemi.randevu.application.port.out.PatientRepository;
import com.hastanesistemi.randevu.application.service.RandevuServisi;
import com.hastanesistemi.randevu.domain.exception.InvalidAppointmentStateException;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.common.TimeSlot;
import com.hastanesistemi.randevu.domain.model.facility.HealthcareFacility;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;
import com.hastanesistemi.randevu.domain.model.person.Role;
import com.hastanesistemi.randevu.domain.model.service.MedicalService;
import com.hastanesistemi.randevu.infrastructure.security.RandevuKullanicisi;
import com.hastanesistemi.randevu.presentation.dto.randevu.RandevuOlusturIstegi;
import com.hastanesistemi.randevu.presentation.dto.randevu.RandevuYanit;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Randevu olusturma/onaylama/iptal/tamamlama ve listeleme endpoint'leri.
 * <p>
 * Bu controller KASITLI OLARAK "ince"dir: gercek is kurallari (cakisma,
 * durum gecisi, uygunluk kontrolu) {@code RandevuServisi} (application) ve
 * {@code Appointment}/{@code Doctor} (domain) icindedir. Controller'in tek
 * sorumlulugu, HTTP istegini (DTO) domain nesnelerine cevirmek, use-case'i
 * cagirmak ve sonucu tekrar DTO'ya cevirmektir - bkz. {@code RandevuServisi}
 * ic dokumantasyonu (ayni "ince servis" felsefesi).
 */
@RestController
@RequestMapping("/api/randevular")
public class RandevuController {

    private final RandevuServisi randevuServisi;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final HealthcareFacilityRepository healthcareFacilityRepository;

    public RandevuController(RandevuServisi randevuServisi, AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository, DoctorRepository doctorRepository,
                              MedicalServiceRepository medicalServiceRepository,
                              HealthcareFacilityRepository healthcareFacilityRepository) {
        this.randevuServisi = randevuServisi;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicalServiceRepository = medicalServiceRepository;
        this.healthcareFacilityRepository = healthcareFacilityRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('HASTA')")
    public ResponseEntity<RandevuYanit> randevuOlustur(@Valid @RequestBody RandevuOlusturIstegi istek,
                                                         @AuthenticationPrincipal RandevuKullanicisi kullanici) {
        Patient hasta = hastaBul(kullanici.getKullaniciId());
        Doctor doktor = doktorBul(istek.doktorId());
        MedicalService hizmet = hizmetBul(istek.hizmetId());
        HealthcareFacility tesis = tesisBul(istek.tesisId());
        TimeSlot zamanAraligi = new TimeSlot(istek.baslangic(), istek.bitis());

        Appointment randevu = randevuServisi.randevuOlustur(hasta, doktor, hizmet, tesis, zamanAraligi);
        return ResponseEntity.status(HttpStatus.CREATED).body(yanitaCevir(randevu));
    }

    @PostMapping("/{id}/onayla")
    @PreAuthorize("hasAnyRole('DOKTOR', 'SAGLIK_PERSONELI')")
    public RandevuYanit randevuOnayla(@PathVariable Long id) {
        return yanitaCevir(randevuServisi.randevuOnayla(id));
    }

    @PostMapping("/{id}/tamamla")
    @PreAuthorize("hasAnyRole('DOKTOR', 'SAGLIK_PERSONELI')")
    public RandevuYanit randevuTamamla(@PathVariable Long id) {
        return yanitaCevir(randevuServisi.randevuTamamla(id));
    }

    /** Hasta SADECE KENDI randevusunu iptal edebilir; doktor/personel/yonetici herhangi birini iptal edebilir. */
    @PostMapping("/{id}/iptal")
    public RandevuYanit randevuIptalEt(@PathVariable Long id, @AuthenticationPrincipal RandevuKullanicisi kullanici) {
        kendiRandevusuMuDogrula(id, kullanici);
        return yanitaCevir(randevuServisi.randevuIptalEt(id));
    }

    @GetMapping("/hastam")
    @PreAuthorize("hasRole('HASTA')")
    public List<RandevuYanit> kendiRandevularim(@AuthenticationPrincipal RandevuKullanicisi kullanici) {
        Patient hasta = hastaBul(kullanici.getKullaniciId());
        return appointmentRepository.hastaninRandevulariniGetir(hasta).stream().map(this::yanitaCevir).toList();
    }

    @GetMapping("/doktorum")
    @PreAuthorize("hasAnyRole('DOKTOR', 'SAGLIK_PERSONELI')")
    public List<RandevuYanit> doktorRandevularim(@AuthenticationPrincipal RandevuKullanicisi kullanici) {
        Doctor doktor = doktorBul(kullanici.getKullaniciId());
        return appointmentRepository.doktorunRandevulariniGetir(doktor).stream().map(this::yanitaCevir).toList();
    }

    private void kendiRandevusuMuDogrula(Long randevuId, RandevuKullanicisi kullanici) {
        if (kullanici.getRol() != Role.HASTA) {
            return;
        }
        Appointment randevu = appointmentRepository.idIleBul(randevuId)
                .orElseThrow(() -> new InvalidAppointmentStateException("Randevu bulunamadi: id=" + randevuId));
        if (!randevu.getHasta().getId().equals(kullanici.getKullaniciId())) {
            throw new AccessDeniedException("Baska bir hastanin randevusunu iptal edemezsiniz.");
        }
    }

    private RandevuYanit yanitaCevir(Appointment randevu) {
        return RandevuYanit.fromDomain(randevu, randevuServisi.efektifUcretHesapla(randevu));
    }

    private Patient hastaBul(Long id) {
        return patientRepository.idIleBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Hasta bulunamadi: id=" + id));
    }

    private Doctor doktorBul(Long id) {
        return doctorRepository.idIleBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Doktor bulunamadi: id=" + id));
    }

    private MedicalService hizmetBul(Long id) {
        return medicalServiceRepository.idIleBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Hizmet bulunamadi: id=" + id));
    }

    private HealthcareFacility tesisBul(Long id) {
        return healthcareFacilityRepository.idIleBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Tesis bulunamadi: id=" + id));
    }
}
