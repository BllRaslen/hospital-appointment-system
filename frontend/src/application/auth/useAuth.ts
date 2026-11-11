"use client";

import { useContext } from "react";
import { AuthContext } from "@/application/auth/AuthContext";

/**
 * {@code AuthContext}'i tuketen use-case hook'u. {@code AuthProvider}
 * disinda cagrilirsa (programci hatasi) acikca hata firlatir - sessizce
 * {@code undefined} donup cagiran koda belirsiz bir "null" kontrolu
 * yaptirmak yerine, hatanin NEREDE oldugunu hemen gosterir.
 */
export function useAuth() {
  const baglam = useContext(AuthContext);
  if (!baglam) {
    throw new Error("useAuth, AuthProvider icinde kullanilmalidir.");
  }
  return baglam;
}
