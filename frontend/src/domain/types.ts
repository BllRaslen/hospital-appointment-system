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
  tesisAdi: string;
  baslangic: string; // ISO-8601 tarih/saat
  bitis: string; // ISO-8601 tarih/saat
  durum: RandevuDurumu;
  efektifUcret: number;
}

export interface Hizmet {
  id: number;
  ad: string;
  aciklama?: string;
  sureDakika: number;
  ucret: number;
}

export interface Tesis {
  id: number;
  ad: string;
  tesisTuru: string;
  sehir?: string;
  gunlukMaksimumRandevuKapasitesi: number;
}

// Kimlik dogrulama (auth) istek/yanit sozlesmeleri - backend
// presentation/dto/auth paketindeki karsiliklariyla birebir eslesir.

export interface GirisIstegi {
  email: string;
  sifre: string;
}

export interface GirisYaniti {
  token: string;
  kullaniciId: number;
  adSoyad: string;
  rol: Rol;
}

export interface HastaKayitIstegi {
  ad: string;
  soyad: string;
  tcKimlikNo: string;
  email: string;
  telefonNo?: string;
  dogumTarihi?: string; // ISO-8601 tarih (YYYY-MM-DD)
  sifre: string;
  kanGrubu?: string;
  sigortaNo?: string;
}

export interface RandevuOlusturIstegi {
  doktorId: number;
  hizmetId: number;
  tesisId: number;
  baslangic: string; // ISO-8601 tarih/saat
  bitis: string; // ISO-8601 tarih/saat
}
