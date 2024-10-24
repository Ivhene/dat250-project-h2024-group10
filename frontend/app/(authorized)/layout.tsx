import CommonNavbar from "@/components/authorizedPagesComponents/CommonNavbar";

export default function AuthorizedLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="h-full w-full bg-white flex flex-col">
      <CommonNavbar />
      <main className="flex-grow">{children}</main>
    </div>
  );
}
