"use client";

// Application katmani: oturum (auth) durumunu uygulama agacinin tamaminda
// paylasan React Context + Provider. Next.js'te sunucu/istemci bileseni
// ayrimi oldugundan ("use client"), oturum durumu gibi tarayici-tarafi
// durum (localStorage) tasiyan her sey bir Client Component'te yasamalidir.
import { createContext, useCallback, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";
import { girisYap as girisYapIstegi } from "@/infrastructure/api/authApi";
import type { GirisYaniti, Rol } from "@/domain/types";

const DEPOLAMA_ANAHTARI = "randevu-sistemi.oturum";

export interface OturumBilgisi {
  token: string;
  kullaniciId: number;
  adSoyad: string;
  rol: Rol;
}

interface AuthBaglami {
  oturum: OturumBilgisi | null;
  yukleniyor: boolean;
  girisYap: (email: string, sifre: string) => Promise<void>;
  cikisYap: () => void;
}

export const AuthContext = createContext<AuthBaglami | undefined>(undefined);

function oturumuOku(): OturumBilgisi | null {
  if (typeof window === "undefined") {
    return null;
  }
  const ham = window.localStorage.getItem(DEPOLAMA_ANAHTARI);
  if (!ham) {
    return null;
  }
  try {
    return JSON.parse(ham) as OturumBilgisi;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [oturum, setOturum] = useState<OturumBilgisi | null>(null);
  const [yukleniyor, setYukleniyor] = useState(true);

  useEffect(() => {
    setOturum(oturumuOku());
    setYukleniyor(false);
  }, []);

  const girisYap = useCallback(async (email: string, sifre: string) => {
    const yanit: GirisYaniti = await girisYapIstegi({ email, sifre });
    const yeniOturum: OturumBilgisi = {
      token: yanit.token,
      kullaniciId: yanit.kullaniciId,
      adSoyad: yanit.adSoyad,
      rol: yanit.rol,
    };
    window.localStorage.setItem(DEPOLAMA_ANAHTARI, JSON.stringify(yeniOturum));
    setOturum(yeniOturum);
  }, []);

  const cikisYap = useCallback(() => {
    window.localStorage.removeItem(DEPOLAMA_ANAHTARI);
    setOturum(null);
  }, []);

  const deger = useMemo(
    () => ({ oturum, yukleniyor, girisYap, cikisYap }),
    [oturum, yukleniyor, girisYap, cikisYap]
  );

  return <AuthContext.Provider value={deger}>{children}</AuthContext.Provider>;
}
