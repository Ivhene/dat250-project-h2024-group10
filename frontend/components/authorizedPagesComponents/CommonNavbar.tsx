"use client";

import { getUser, signOut } from "@/lib/API";
import { Button } from "../ui/button";
import Link from "next/link";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import CreatePollForm from "./CreateFormPoll";
import { useState } from "react";

export default function CommonNavbar() {
  const [open, setOpen] = useState(false);
  /*const user = await getUser();

  if (!user) {
    return <div>Error</div>;
  } */

  function close() {
    setOpen(false);
  }

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
        <Dialog open={open}>
          <DialogTrigger
            onClick={() => setOpen(!open)}
            className="bg-white p-2 text-sm rounded-lg ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950"
          >
            Create poll
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Create poll</DialogTitle>
            </DialogHeader>
            <DialogDescription>
              Create a poll by filling in the form below
            </DialogDescription>
            <CreatePollForm close={close} />
          </DialogContent>
        </Dialog>
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
