"use client";

import { getUser, signOut } from "@/lib/API";
import { Button } from "../ui/button";
import Link from "next/link";

export default function CommonNavbar() {
  /*const user = await getUser();

  if (!user) {
    return <div>Error</div>;
  } */

  return (
    <nav className="w-full h-18 flex items-center bg-neutral-100">
      <div className="p-4 mr-2">LOGO</div>
      <Link href="/feed" className="p-4">
        Feed
      </Link>
      <Link href="/profile" className="p-4">
        Profile
      </Link>
      <div className="mr-4 ml-auto space-x-4 flex items-center">
        <p className="text-sm">Signed in as USER</p>
        <Button
          onClick={async () => {
            signOut();
          }}
          className="bg-neutral-700 hover:bg-neutral-950"
        >
          Sign out
        </Button>
      </div>
    </nav>
  );
}
