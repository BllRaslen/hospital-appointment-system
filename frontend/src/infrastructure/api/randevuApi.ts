// Backend RandevuController/DoktorController/HizmetController/TesisController
// ile konusan ince sarmalayici (bkz. apiClient.ts). Randevu olusturma akisinin
// (randevu-olustur sayfasi) ihtiyac duydugu tum okuma/yazma istekleri buradadir.
import { apiGet, apiPost } from "@/infrastructure/api/apiClient";
import type { Doktor, Hizmet, Randevu, RandevuOlusturIstegi, Tesis } from "@/domain/types";

export function doktorlariGetir(): Promise<Doktor[]> {
  return apiGet<Doktor[]>("/api/doktorlar");
}

export function hizmetleriGetir(): Promise<Hizmet[]> {
  return apiGet<Hizmet[]>("/api/hizmetler");
}

export function tesisleriGetir(): Promise<Tesis[]> {
  return apiGet<Tesis[]>("/api/tesisler");
}

export function randevuOlustur(istek: RandevuOlusturIstegi, token: string): Promise<Randevu> {
  return apiPost<Randevu>("/api/randevular", istek, token);
}

export function kendiRandevulariniGetir(token: string): Promise<Randevu[]> {
  return apiGet<Randevu[]>("/api/randevular/hastam", token);
}

export function randevuIptalEt(randevuId: number, token: string): Promise<Randevu> {
  return apiPost<Randevu>(`/api/randevular/${randevuId}/iptal`, {}, token);
}
