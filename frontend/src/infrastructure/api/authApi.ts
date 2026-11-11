// Backend AuthController ile konusan ince sarmalayici (bkz. apiClient.ts).
import { apiPost } from "@/infrastructure/api/apiClient";
import type { GirisIstegi, GirisYaniti, Hasta, HastaKayitIstegi } from "@/domain/types";

export function girisYap(istek: GirisIstegi): Promise<GirisYaniti> {
  return apiPost<GirisYaniti>("/api/auth/giris", istek);
}

export function hastaKayitOl(istek: HastaKayitIstegi): Promise<Hasta> {
  return apiPost<Hasta>("/api/auth/kayit", istek);
}
