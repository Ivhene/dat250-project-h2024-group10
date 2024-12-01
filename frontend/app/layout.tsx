import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Feed app",
  description: "Feed app developed during DAT250",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`antialiased h-screen w-full`}>{children}</body>
    </html>
  );
}
