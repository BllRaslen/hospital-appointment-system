"use client";

import Link from "next/link";
import { useAuth } from "@/application/auth/useAuth";

// Ana sayfa: oturum durumuna gore hastaya randevu olusturma/goruntuleme
// akisina veya giris/kayit sayfalarina yonlendiren kisa tanitim sayfasi.
export default function HomePage() {
  const { oturum, yukleniyor } = useAuth();

  return (
    <main>
      <h1>Hastane / Klinik Randevu Sistemi</h1>
      <p>
        Bu proje Next.js (frontend) + Spring Boot (backend) + PostgreSQL ile
        Clean Architecture prensiplerine uygun olarak gelistirilmektedir.
      </p>
      {yukleniyor ? null : oturum ? (
        <p>
          Hos geldiniz, {oturum.adSoyad}. <Link href="/randevu-olustur">Randevu olusturabilir</Link> veya{" "}
          <Link href="/randevularim">mevcut randevularinizi goruntuleyebilirsiniz</Link>.
        </p>
      ) : (
        <p>
          Randevu olusturmak icin once <Link href="/kayit">kayit olun</Link> veya{" "}
          <Link href="/giris">giris yapin</Link>.
        </p>
      )}
    </main>
  );
}
