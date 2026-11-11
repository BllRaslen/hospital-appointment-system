// Infrastructure katmani: backend REST API'siyle HTTP uzerinden konusan
// ince bir sarmalayici (wrapper). Application/presentation katmanlari
// dogrudan "fetch" kullanmak yerine bu modulu kullanacak; boylece ileride
// JWT token ekleme, hata yonetimi gibi detaylar tek bir yerde degisir.

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

// Backend'in GlobalHataYakalayici'sinin dondugu ortak hata govdesi
// (presentation/dto/ortak/HataYaniti) ile birebir eslesir.
export interface ApiHataYaniti {
  zaman?: string;
  durumKodu: number;
  mesaj: string;
  alanHatalari?: string[];
}

export class ApiHatasi extends Error {
  readonly durumKodu: number;
  readonly alanHatalari: string[];

  constructor(durumKodu: number, mesaj: string, alanHatalari: string[] = []) {
    super(mesaj);
    this.durumKodu = durumKodu;
    this.alanHatalari = alanHatalari;
  }
}

async function yanitiIsle<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let govde: ApiHataYaniti | null = null;
    try {
      govde = (await response.json()) as ApiHataYaniti;
    } catch {
      // Govde JSON degilse (orn. 401/403 icin bos govde), varsayilan mesaj kullanilir.
    }
    throw new ApiHatasi(
      response.status,
      govde?.mesaj ?? `API istegi basarisiz: ${response.status} ${response.statusText}`,
      govde?.alanHatalari ?? []
    );
  }
  if (response.status === 204) {
    return undefined as T;
  }
  return response.json() as Promise<T>;
}

function yetkiBasligi(token?: string): Record<string, string> {
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function apiGet<T>(path: string, token?: string): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json", ...yetkiBasligi(token) },
  });
  return yanitiIsle<T>(response);
}

export async function apiPost<T>(path: string, body: unknown, token?: string): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...yetkiBasligi(token) },
    body: JSON.stringify(body),
  });
  return yanitiIsle<T>(response);
}
