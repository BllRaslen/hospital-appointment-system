"use client";

import { useRandevuOlusturFormu } from "@/application/randevu/useRandevuOlusturFormu";

/** Randevu olusturma formu: doktor/hizmet/tesis secimi + zaman araligi. */
export function RandevuOlusturFormu() {
  const {
    doktorlar,
    hizmetler,
    tesisler,
    secenekleriYukleniyor,
    doktorId,
    setDoktorId,
    hizmetId,
    setHizmetId,
    tesisId,
    setTesisId,
    baslangic,
    setBaslangic,
    bitis,
    setBitis,
    hata,
    basariMesaji,
    gonderiliyor,
    gonder,
  } = useRandevuOlusturFormu();

  if (secenekleriYukleniyor) {
    return <p>Secenekler yukleniyor...</p>;
  }

  return (
    <form className="form" onSubmit={gonder}>
      <label className="form-alan">
        Doktor
        <select value={doktorId} onChange={(e) => setDoktorId(e.target.value)} required>
          <option value="" disabled>
            Doktor secin
          </option>
          {doktorlar.map((doktor) => (
            <option key={doktor.id} value={doktor.id}>
              {doktor.ad} {doktor.soyad} - {doktor.uzmanlikAlani}
            </option>
          ))}
        </select>
      </label>
      <label className="form-alan">
        Hizmet
        <select value={hizmetId} onChange={(e) => setHizmetId(e.target.value)} required>
          <option value="" disabled>
            Hizmet secin
          </option>
          {hizmetler.map((hizmet) => (
            <option key={hizmet.id} value={hizmet.id}>
              {hizmet.ad} ({hizmet.sureDakika} dk, {hizmet.ucret} TL)
            </option>
          ))}
        </select>
      </label>
      <label className="form-alan">
        Tesis
        <select value={tesisId} onChange={(e) => setTesisId(e.target.value)} required>
          <option value="" disabled>
            Tesis secin
          </option>
          {tesisler.map((tesis) => (
            <option key={tesis.id} value={tesis.id}>
              {tesis.ad} ({tesis.tesisTuru})
            </option>
          ))}
        </select>
      </label>
      <label className="form-alan">
        Baslangic
        <input type="datetime-local" value={baslangic} onChange={(e) => setBaslangic(e.target.value)} required />
      </label>
      <label className="form-alan">
        Bitis
        <input type="datetime-local" value={bitis} onChange={(e) => setBitis(e.target.value)} required />
      </label>
      {hata && <p className="form-hata">{hata}</p>}
      {basariMesaji && <p className="form-basari">{basariMesaji}</p>}
      <button type="submit" disabled={gonderiliyor}>
        {gonderiliyor ? "Gonderiliyor..." : "Randevu Olustur"}
      </button>
    </form>
  );
}
