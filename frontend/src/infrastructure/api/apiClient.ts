// Infrastructure katmani: backend REST API'siyle HTTP uzerinden konusan
// ince bir sarmalayici (wrapper). Application/presentation katmanlari
// dogrudan "fetch" kullanmak yerine bu modulu kullanacak; boylece ileride
// JWT token ekleme, hata yonetimi gibi detaylar tek bir yerde degisir.
//
// Durum: Kimlik dogrulama (JWT) baslik (header) eklemesi, backend auth
// endpoint'leri hazir oldugunda eklenecektir.

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export async function apiGet<T>(path: string): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    throw new Error(`API istegi basarisiz: ${response.status} ${response.statusText}`);
  }
  return response.json() as Promise<T>;
}

export async function apiPost<T>(path: string, body: unknown): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!response.ok) {
    throw new Error(`API istegi basarisiz: ${response.status} ${response.statusText}`);
  }
  return response.json() as Promise<T>;
}
