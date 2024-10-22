"use client";

import LandingNavbar from "@/components/landingPage/LandingNavbar";
import { Button } from "@/components/ui/button";
import { createUser, getUserByUsername } from "@/lib/API";

export default function Home() {
  const panelStyling = "w-72 bg-neutral-100 p-4 rounded-xl text-center h-52";

  return (
    <div className="h-full w-full">
      <LandingNavbar />
      <div className="">
        Adding a screenshot of main logged in page blurred out later
      </div>
      <div className="w-full flex flex-row gap-16 justify-center">
        <div className={`${panelStyling} flex flex-col justify-between`}>
          <div className="space-y-3">
            <h2 className="font-extrabold text-xl">Already a user?</h2>
            <p className="text-neutral-600">Click the button below to log in</p>
          </div>
          <Button
            onClick={async () => {
              getUserByUsername("tester");
            }}
            className="bg-white ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950"
          >
            login
          </Button>
        </div>
        <div className={`${panelStyling} flex flex-col justify-between`}>
          <div className="space-y-3">
            <h2 className="font-extrabold text-xl">Want to get started?</h2>
            <p className="text-neutral-600">
              Click the button below to create your account
            </p>
          </div>
          <Button
            onClick={async () => {
              createUser({
                username: "tester",
                email: "tester@gmail.com",
                votes: [],
                polls: [],
              });
            }}
            className="bg-white ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950"
          >
            create user
          </Button>
        </div>
      </div>
    </div>
  );
}
