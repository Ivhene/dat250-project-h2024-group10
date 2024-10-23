import { createUser, getUserByUsername, signOut } from "@/lib/API";
import { Button } from "../ui/button";

export default function LandingNavbar() {
  return (
    <nav className="w-full h-18 p-4 flex items-center bg-neutral-100">
      <div>LOGO</div>
      <div className="mr-0 ml-auto space-x-4">
        <Button
          onClick={async () => {
            signOut();
          }}
          className="bg-neutral-700 hover:bg-neutral-950"
        >
          TEMP SIGN OUT
        </Button>
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
    </nav>
  );
}
