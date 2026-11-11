"use client";

import { useState } from "react";
import type { FormEvent } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/application/auth/useAuth";
import { ApiHatasi } from "@/infrastructure/api/apiClient";

/** Giris (login) formu. Basarili girişte ana sayfaya yonlendirir. */
export function GirisFormu() {
  const { girisYap } = useAuth();
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [sifre, setSifre] = useState("");
  const [hata, setHata] = useState<string | null>(null);
  const [gonderiliyor, setGonderiliyor] = useState(false);

  async function gonder(olay: FormEvent<HTMLFormElement>) {
    olay.preventDefault();
    setHata(null);
    setGonderiliyor(true);
    try {
      await girisYap(email, sifre);
      router.push("/");
    } catch (e) {
      setHata(e instanceof ApiHatasi ? e.message : "Giris yapilamadi. Lutfen tekrar deneyin.");
    } finally {
      setGonderiliyor(false);
    }
  }

  return (
    <form className="form" onSubmit={gonder}>
      <label className="form-alan">
        E-posta
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      </label>
      <label className="form-alan">
        Sifre
        <input type="password" value={sifre} onChange={(e) => setSifre(e.target.value)} required />
      </label>
      {hata && <p className="form-hata">{hata}</p>}
      <button type="submit" disabled={gonderiliyor}>
        {gonderiliyor ? "Giris yapiliyor..." : "Giris Yap"}
      </button>
    </form>
  );
}
