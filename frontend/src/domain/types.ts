// Bu dosya, backend REST API'sinin dondurecegi veri sekillerini (DTO)
// tanimlar. Frontend tarafinda "domain" katmani, backend'deki gibi zengin
// bir OOP sinif hiyerarsisi degil, API sozlesmesini temsil eden TypeScript
// tipleridir (Next.js/React ekosisteminde idiyomatik yaklasim budur; asil
// OOP is kurallari backend'in domain katmaninda yasar).

export type Rol = "HASTA" | "DOKTOR" | "SAGLIK_PERSONELI" | "YONETICI";

export type RandevuDurumu =
  | "BEKLEMEDE"
  | "ONAYLANDI"
  | "IPTAL_EDILDI"
  | "TAMAMLANDI"
  | "GELMEDI";

export interface Hasta {
  id: number;
  ad: string;
  soyad: string;
  email: string;
  telefonNo?: string;
}

export interface Doktor {
  id: number;
  ad: string;
  soyad: string;
  uzmanlikAlani: string;
}

export interface Randevu {
  id: number;
  hasta: Hasta;
  doktor: Doktor;
  hizmetAdi: string;
  baslangic: string; // ISO-8601 tarih/saat
  bitis: string; // ISO-8601 tarih/saat
  durum: RandevuDurumu;
}
