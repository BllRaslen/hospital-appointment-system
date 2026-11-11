"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/application/auth/useAuth";
import { kendiRandevulariniGetir, randevuIptalEt } from "@/infrastructure/api/randevuApi";
import { ApiHatasi } from "@/infrastructure/api/apiClient";
import type { Randevu } from "@/domain/types";

/** Hastanin kendi randevularini listeleyip iptal edebildigi sayfa. */
export default function RandevularimPage() {
  const { oturum, yukleniyor } = useAuth();
  const router = useRouter();

  const [randevular, setRandevular] = useState<Randevu[]>([]);
  const [yukleniyorMu, setYukleniyorMu] = useState(true);
  const [hata, setHata] = useState<string | null>(null);

  useEffect(() => {
    if (yukleniyor) {
      return;
    }
    if (!oturum) {
      router.push("/giris");
      return;
    }
    kendiRandevulariniGetir(oturum.token)
      .then(setRandevular)
      .catch((e) => setHata(e instanceof Error ? e.message : "Randevular yuklenemedi."))
      .finally(() => setYukleniyorMu(false));
  }, [oturum, yukleniyor, router]);

  async function iptalEt(randevuId: number) {
    if (!oturum) {
      return;
    }
    try {
      const guncellenen = await randevuIptalEt(randevuId, oturum.token);
      setRandevular((oncekiler) => oncekiler.map((r) => (r.id === guncellenen.id ? guncellenen : r)));
    } catch (e) {
      setHata(e instanceof ApiHatasi ? e.message : "Randevu iptal edilemedi.");
    }
  }

  if (yukleniyor || yukleniyorMu) {
    return <p>Yukleniyor...</p>;
  }

  return (
    <main>
      <h1>Randevularim</h1>
      {hata && <p className="form-hata">{hata}</p>}
      {randevular.length === 0 ? (
        <p>Henuz bir randevunuz yok.</p>
      ) : (
        <ul className="randevu-listesi">
          {randevular.map((randevu) => (
            <li key={randevu.id} className="randevu-karti">
              <p>
                <strong>{randevu.doktor.ad} {randevu.doktor.soyad}</strong> - {randevu.hizmetAdi}
              </p>
              <p>{randevu.tesisAdi}</p>
              <p>
                {new Date(randevu.baslangic).toLocaleString("tr-TR")} - {new Date(randevu.bitis).toLocaleString("tr-TR")}
              </p>
              <p>Durum: {randevu.durum} | Ucret: {randevu.efektifUcret} TL</p>
              {(randevu.durum === "BEKLEMEDE" || randevu.durum === "ONAYLANDI") && (
                <button type="button" onClick={() => iptalEt(randevu.id)}>
                  Iptal Et
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}
