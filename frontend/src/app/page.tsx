// Gecici ana sayfa: proje iskeleti asamasinda sadece durum bilgisini gosterir.
// Gercek giris/randevu ekranlari, backend API'si (presentation katmani)
// hazir oldukca ilerleyen gunlerde eklenecektir.
export default function HomePage() {
  return (
    <main>
      <h1>Hastane / Klinik Randevu Sistemi</h1>
      <p>
        Bu proje Next.js (frontend) + Spring Boot (backend) + PostgreSQL ile
        Clean Architecture prensiplerine uygun olarak gelistirilmektedir.
      </p>
      <p>Mevcut durum: Proje iskeleti ve backend domain (OOP) katmani tamamlandi.</p>
    </main>
  );
}
