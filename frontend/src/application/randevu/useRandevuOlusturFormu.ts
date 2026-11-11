"use client";

import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/application/auth/useAuth";
import { doktorlariGetir, hizmetleriGetir, randevuOlustur, tesisleriGetir } from "@/infrastructure/api/randevuApi";
import { ApiHatasi } from "@/infrastructure/api/apiClient";
import type { Doktor, Hizmet, Tesis } from "@/domain/types";

/**
 * Randevu olusturma sayfasinin tum durum/davranis mantigini (secenek
 * listelerini yukleme, form alanlari, gonderme) tasiyan use-case hook'u.
 * {@code RandevuOlusturFormu} (presentation) bu hook'u tuketir; boylece UI
 * bileseni sadece GORUNUMDEN sorumlu kalir.
 */
export function useRandevuOlusturFormu() {
  const { oturum } = useAuth();
  const router = useRouter();

  const [doktorlar, setDoktorlar] = useState<Doktor[]>([]);
  const [hizmetler, setHizmetler] = useState<Hizmet[]>([]);
  const [tesisler, setTesisler] = useState<Tesis[]>([]);
  const [secenekleriYukleniyor, setSecenekleriYukleniyor] = useState(true);

  const [doktorId, setDoktorId] = useState("");
  const [hizmetId, setHizmetId] = useState("");
  const [tesisId, setTesisId] = useState("");
  const [baslangic, setBaslangic] = useState("");
  const [bitis, setBitis] = useState("");

  const [hata, setHata] = useState<string | null>(null);
  const [basariMesaji, setBasariMesaji] = useState<string | null>(null);
  const [gonderiliyor, setGonderiliyor] = useState(false);

  useEffect(() => {
    Promise.all([doktorlariGetir(), hizmetleriGetir(), tesisleriGetir()])
      .then(([d, h, t]) => {
        setDoktorlar(d);
        setHizmetler(h);
        setTesisler(t);
      })
      .catch((e) => setHata(e instanceof Error ? e.message : "Secenekler yuklenemedi."))
      .finally(() => setSecenekleriYukleniyor(false));
  }, []);

  async function gonder(olay: FormEvent<HTMLFormElement>) {
    olay.preventDefault();
    if (!oturum) {
      router.push("/giris");
      return;
    }
    setHata(null);
    setBasariMesaji(null);
    setGonderiliyor(true);
    try {
      await randevuOlustur(
        {
          doktorId: Number(doktorId),
          hizmetId: Number(hizmetId),
          tesisId: Number(tesisId),
          baslangic,
          bitis,
        },
        oturum.token
      );
      setBasariMesaji("Randevunuz olusturuldu. Onay icin bekleyiniz.");
    } catch (e) {
      setHata(e instanceof ApiHatasi ? e.message : "Randevu olusturulamadi.");
    } finally {
      setGonderiliyor(false);
    }
  }

  return {
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
  };
}
