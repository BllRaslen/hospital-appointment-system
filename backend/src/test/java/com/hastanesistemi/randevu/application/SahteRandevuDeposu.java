package com.hastanesistemi.randevu.application;

import com.hastanesistemi.randevu.application.port.out.AppointmentRepository;
import com.hastanesistemi.randevu.domain.model.appointment.Appointment;
import com.hastanesistemi.randevu.domain.model.person.Doctor;
import com.hastanesistemi.randevu.domain.model.person.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link AppointmentRepository} portunun, herhangi bir veritabani (JPA/
 * PostgreSQL) olmadan TESTLERDE kullanilan bellek-ici (in-memory) sahte
 * implementasyonu.
 * <p>
 * Bu sinifin varligi, Repository deseninin (port/adapter) en buyuk pratik
 * faydasini gosterir: {@code RandevuServisi}, gercek veritabani
 * altyapisi (infrastructure katmani) henuz yazilmamis olsa dahi, Spring
 * Boot/PostgreSQL baglantisi kurmadan, hizli ve izole birim testleriyle
 * dogrulanabilir.
 */
class SahteRandevuDeposu implements AppointmentRepository {

    private final Map<Long, Appointment> depo = new LinkedHashMap<>();
    private final AtomicLong idSayaci = new AtomicLong(0);

    @Override
    public Appointment kaydet(Appointment randevu) {
        if (randevu.getId() == null) {
            randevu.idAta(idSayaci.incrementAndGet());
        }
        depo.put(randevu.getId(), randevu);
        return randevu;
    }

    @Override
    public Optional<Appointment> idIleBul(Long id) {
        return Optional.ofNullable(depo.get(id));
    }

    @Override
    public List<Appointment> doktorunRandevulariniGetir(Doctor doktor) {
        List<Appointment> sonuc = new ArrayList<>();
        for (Appointment randevu : depo.values()) {
            if (randevu.getDoktor().equals(doktor)) {
                sonuc.add(randevu);
            }
        }
        return sonuc;
    }

    @Override
    public List<Appointment> hastaninRandevulariniGetir(Patient hasta) {
        throw new UnsupportedOperationException("Henuz uygulanmadi.");
    }

    @Override
    public List<Appointment> hepsiniGetir() {
        throw new UnsupportedOperationException("Henuz uygulanmadi.");
    }
}
