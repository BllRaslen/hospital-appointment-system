/**
 * SUNUM (Presentation) katmani.
 * <p>
 * REST API controller'lari ({@code AuthController}, {@code
 * RandevuController}, {@code DoktorController}, {@code HizmetController},
 * {@code TesisController}, {@code HastaController}), istek/yanit DTO'lari
 * ({@code dto} alt paketleri) ve global hata yakalayici ({@code
 * GlobalHataYakalayici} - {@code @RestControllerAdvice} ile DomainException
 * -&gt; HTTP durum kodu eslemesi) bu katmandadir. Next.js frontend, bu
 * katmanin actigi HTTP endpoint'leri uzerinden backend ile konusur.
 * <p>
 * Bu katman KASITLI OLARAK "ince"dir: gercek is kurallari application/domain
 * katmanlarinda kalir (bkz. {@code RandevuServisi}, {@code Appointment},
 * {@code Doctor} ic dokumantasyonlari); controller'lar sadece HTTP &lt;-&gt;
 * domain donusumunu ve yetkilendirmeyi ({@code @PreAuthorize}) yonetir.
 */
package com.hastanesistemi.randevu.presentation;
