import type { Metadata } from "next";
import "./globals.css";
import { AuthProvider } from "@/application/auth/AuthContext";
import { Navbar } from "@/presentation/components/Navbar";

export const metadata: Metadata = {
  title: "Hastane Randevu Sistemi",
  description: "Hastane / Klinik Randevu Sistemi - Next.js Frontend",
};

// Kok layout: tum sayfalar bu govdeyi (html/body) paylasir. AuthProvider,
// oturum durumunu (bkz. application/auth/AuthContext.tsx) butun sayfa
// agacina paylastirir; Navbar bu durumu her sayfada tutarli sekilde gosterir.
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="tr">
      <body>
        <AuthProvider>
          <Navbar />
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
