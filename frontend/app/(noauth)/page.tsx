"use client";

import { Button } from "@/components/ui/button";
import { createUser, getUserByUsername } from "@/lib/API";
import Image from "next/image";

export default function Home() {
  return (
    <div className="h-screen w-full p-4 space-x-4">
      <Button
        onClick={async () => {
          getUserByUsername("tester");
        }}
        className="bg-white text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950"
      >
        login
      </Button>
      <Button
        onClick={async () => {
          createUser({
            username: "tester",
            email: "tester@gmail.com",
            votes: [],
            polls: [],
          });
        }}
        className="bg-neutral-700 hover:bg-neutral-950"
      >
        create user
      </Button>
    </div>
  );
}
