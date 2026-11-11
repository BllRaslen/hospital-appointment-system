package com.hastanesistemi.randevu.presentation;

import com.hastanesistemi.randevu.domain.exception.AppointmentConflictException;
import com.hastanesistemi.randevu.domain.exception.DomainException;
import com.hastanesistemi.randevu.domain.exception.InvalidAppointmentStateException;
import com.hastanesistemi.randevu.domain.exception.InvalidAvailabilityException;
import com.hastanesistemi.randevu.presentation.dto.ortak.HataYaniti;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Tum presentation katmanindaki (REST controller) istisnalari YAKALAYIP
 * tutarli bir HTTP yanitina ceviren merkezi hata yakalayici.
 * <p>
 * OOP ilkesi: COK BICIMLILIK - {@code cakismaVeDurumHatalari}, TEK bir
 * handler'in {@code AppointmentConflictException} ve {@code
 * InvalidAppointmentStateException}'i (her ikisi de {@code DomainException}
 * alt sinifi) polimorfik olarak yakalamasini saglar (bkz. {@code
 * DomainException} ic dokumantasyonu). Spring, birden fazla handler
 * eslesebildiginde EN SPESIFIK eslesen handler'i secer; bu yuzden {@code
 * InvalidAvailabilityException} kendi, daha spesifik HTTP durum kodu
 * (400) gerektiren handler'inda ayri yakalanir.
 */
@RestControllerAdvice
public class GlobalHataYakalayici {

    @ExceptionHandler({AppointmentConflictException.class, InvalidAppointmentStateException.class})
    public ResponseEntity<HataYaniti> cakismaVeDurumHatalari(DomainException hata) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(HataYaniti.olustur(HttpStatus.CONFLICT.value(), hata.getMessage()));
    }

    @ExceptionHandler(InvalidAvailabilityException.class)
    public ResponseEntity<HataYaniti> uygunlukHatasi(InvalidAvailabilityException hata) {
        return ResponseEntity.badRequest()
                .body(HataYaniti.olustur(HttpStatus.BAD_REQUEST.value(), hata.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HataYaniti> gecersizArgumanHatasi(IllegalArgumentException hata) {
        return ResponseEntity.badRequest()
                .body(HataYaniti.olustur(HttpStatus.BAD_REQUEST.value(), hata.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HataYaniti> dogrulamaHatasi(MethodArgumentNotValidException hata) {
        List<String> alanHatalari = hata.getBindingResult().getFieldErrors().stream()
                .map(alan -> alan.getField() + ": " + alan.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(HataYaniti.alanHatalariIle(
                HttpStatus.BAD_REQUEST.value(), "Gonderilen veri gecersiz.", alanHatalari));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<HataYaniti> kimlikDogrulamaHatasi() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(HataYaniti.olustur(HttpStatus.UNAUTHORIZED.value(), "E-posta veya sifre hatali."));
    }
}
