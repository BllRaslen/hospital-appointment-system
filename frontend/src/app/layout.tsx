import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Hastane Randevu Sistemi",
  description: "Hastane / Klinik Randevu Sistemi - Next.js Frontend",
};

// Kok layout: tum sayfalar bu govdeyi (html/body) paylasir.
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="tr">
      <body>{children}</body>
    </html>
  );
}
