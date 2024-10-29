"use client";

import LandingNavbar from "@/components/landingPage/LandingNavbar";
import SigninForm from "@/components/landingPage/SigninForm";
import SignupForm from "@/components/landingPage/SignupForm";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import Image from "next/image";

export default function Home() {
  const panelStyling =
    "w-72 bg-neutral-100 p-4 rounded-xl text-center h-52 border border-neutral-300 shadow-lg";

  return (
    <div className="h-full w-full">
      <LandingNavbar />
      <div className="w-full h-auto">
        <Image
          src="data:image/png;base64,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"
          alt="Image"
          width={300}
          height={300}
          className="w-full h-auto"
        />
      </div>
      <div className="w-full flex flex-row gap-16 justify-center p-8">
        <div className={`${panelStyling} flex flex-col justify-between`}>
          <div className="space-y-3">
            <h2 className="font-extrabold text-xl">Already a user?</h2>
            <p className="text-neutral-600">Click the button below to log in</p>
          </div>
          <Dialog>
            <DialogTrigger className="bg-white p-2 text-sm rounded-lg ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950">
              Sign in
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Sign in</DialogTitle>
              </DialogHeader>
              <DialogDescription>
                Log in by writing your email or username below (TEMP USE
                USERNAME)
              </DialogDescription>
              <SigninForm />
            </DialogContent>
          </Dialog>
        </div>
        <div className={`${panelStyling} flex flex-col justify-between`}>
          <div className="space-y-3">
            <h2 className="font-extrabold text-xl">Want to get started?</h2>
            <p className="text-neutral-600">
              Click the button below to create your account
            </p>
          </div>
          <Dialog>
            <DialogTrigger className="bg-white p-2 text-sm rounded-lg ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950">
              Sign up
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Sign up</DialogTitle>
              </DialogHeader>
              <DialogDescription>
                Create a user by filling in the form below
              </DialogDescription>
              <SignupForm />
            </DialogContent>
          </Dialog>
        </div>
      </div>
    </div>
  );
}
