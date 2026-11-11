"use client";

import { useState } from "react";
import type { FormEvent } from "react";
import { useRouter } from "next/navigation";
import { hastaKayitOl } from "@/infrastructure/api/authApi";
import { ApiHatasi } from "@/infrastructure/api/apiClient";
import type { HastaKayitIstegi } from "@/domain/types";

const BOS_ISTEK: HastaKayitIstegi = {
  ad: "",
  soyad: "",
  tcKimlikNo: "",
  email: "",
  telefonNo: "",
  dogumTarihi: "",
  sifre: "",
  kanGrubu: "",
  sigortaNo: "",
};

/** Hasta kendi kendine kayit (self sign-up) formu. Basarili kayitta giris sayfasina yonlendirir. */
export function KayitFormu() {
  const router = useRouter();
  const [istek, setIstek] = useState<HastaKayitIstegi>(BOS_ISTEK);
  const [hata, setHata] = useState<string[] | null>(null);
  const [gonderiliyor, setGonderiliyor] = useState(false);

  function alanGuncelle<K extends keyof HastaKayitIstegi>(alan: K, deger: string) {
    setIstek((oncekiIstek) => ({ ...oncekiIstek, [alan]: deger }));
  }

  async function gonder(olay: FormEvent<HTMLFormElement>) {
    olay.preventDefault();
    setHata(null);
    setGonderiliyor(true);
    try {
      await hastaKayitOl(istek);
      router.push("/giris");
    } catch (e) {
      setHata(e instanceof ApiHatasi && e.alanHatalari.length > 0 ? e.alanHatalari : [(e as Error).message]);
    } finally {
      setGonderiliyor(false);
    }
  }

  return (
    <form className="form" onSubmit={gonder}>
      <label className="form-alan">
        Ad
        <input value={istek.ad} onChange={(e) => alanGuncelle("ad", e.target.value)} required />
      </label>
      <label className="form-alan">
        Soyad
        <input value={istek.soyad} onChange={(e) => alanGuncelle("soyad", e.target.value)} required />
      </label>
      <label className="form-alan">
        TC Kimlik No
        <input
          value={istek.tcKimlikNo}
          onChange={(e) => alanGuncelle("tcKimlikNo", e.target.value)}
          maxLength={11}
          required
        />
      </label>
      <label className="form-alan">
        E-posta
        <input type="email" value={istek.email} onChange={(e) => alanGuncelle("email", e.target.value)} required />
      </label>
      <label className="form-alan">
        Telefon No
        <input value={istek.telefonNo} onChange={(e) => alanGuncelle("telefonNo", e.target.value)} />
      </label>
      <label className="form-alan">
        Dogum Tarihi
        <input type="date" value={istek.dogumTarihi} onChange={(e) => alanGuncelle("dogumTarihi", e.target.value)} />
      </label>
      <label className="form-alan">
        Sifre
        <input
          type="password"
          value={istek.sifre}
          onChange={(e) => alanGuncelle("sifre", e.target.value)}
          minLength={6}
          required
        />
      </label>
      {hata && (
        <ul className="form-hata">
          {hata.map((mesaj) => (
            <li key={mesaj}>{mesaj}</li>
          ))}
        </ul>
      )}
      <button type="submit" disabled={gonderiliyor}>
        {gonderiliyor ? "Kayit olunuyor..." : "Kayit Ol"}
      </button>
    </form>
  );
}
