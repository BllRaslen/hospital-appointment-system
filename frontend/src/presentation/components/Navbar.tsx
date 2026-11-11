"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useAuth } from "@/application/auth/useAuth";

/** Oturum durumuna gore farkli linkler gosteren ust navigasyon cubugu. */
export function Navbar() {
  const { oturum, yukleniyor, cikisYap } = useAuth();
  const router = useRouter();

  function cikisYapVeYonlendir() {
    cikisYap();
    router.push("/");
  }

  return (
    <nav className="navbar">
      <Link href="/" className="navbar-marka">
        Randevu Sistemi
      </Link>
      <div className="navbar-baglantilar">
        {yukleniyor ? null : oturum ? (
          <>
            <Link href="/randevu-olustur">Randevu Al</Link>
            <Link href="/randevularim">Randevularim</Link>
            <span className="navbar-kullanici">{oturum.adSoyad}</span>
            <button type="button" onClick={cikisYapVeYonlendir}>
              Cikis Yap
            </button>
          </>
        ) : (
          <>
            <Link href="/giris">Giris Yap</Link>
            <Link href="/kayit">Kayit Ol</Link>
          </>
        )}
      </div>
    </nav>
  );
}
