import type { Metadata } from "next";
import "./globals.css";
import RenderTimeTracker from "@/components/AppWrapper";
import AppWrapper from "@/components/AppWrapper";

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
      <body className={`antialiased h-screen w-full`}>
        <AppWrapper children={children}></AppWrapper>
      </body>
    </html>
  );
}
