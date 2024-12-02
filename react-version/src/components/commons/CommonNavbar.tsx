import { Button } from "../ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import CreatePollForm from "./CreateFormPoll";
import { useEffect, useState } from "react";
import { getUserByUsername, getUserFromToken } from "../../lib/API";
import SigninForm from "../landingPage/SigninForm";
import SignupForm from "../landingPage/SignupForm";
import { NavigationProp } from "../../lib/types";

export default function CommonNavbar({ navigate }: NavigationProp) {
  const [open, setOpen] = useState(false);

  const [user, setUser] = useState(""); // State to hold the user

  useEffect(() => {
    // Fetch token and user details
    const fetchUser = async () => {
      const token = localStorage.getItem("authToken");
      if (token) {
        try {
          const fetchedUser = (await getUserFromToken(token)).username;
          setUser(fetchedUser);
        } catch (error) {
          console.error("Error fetching user:", error);
          setUser("");
        }
      }
    };

    fetchUser();
  }, []);

  /*if (user === "") {
    return <div>Error</div>;
  }*/

  function close() {
    setOpen(false);
  }

  function signOut() {
    localStorage.removeItem("authToken");
  }

  return (
    <nav className="w-full h-18 flex items-center bg-neutral-100">
      {/*<div className="p-4 mr-2">LOGO</div>*/}
      <div onClick={() => navigate("landing")} className="p-4">
        Home
      </div>
      <div onClick={() => navigate("feed")} className="p-4">
        Feed
      </div>
      {user !== "" ? (
        <div onClick={() => navigate("user")} className="p-4">
          Profile
        </div>
      ) : null}
      <div className="mr-4 ml-auto space-x-4 flex items-center">
        {user !== "" ? (
          <>
            <p className="text-sm">Signed in as {user}</p>
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
          </>
        ) : (
          <>
            <Dialog>
              <DialogTrigger className="bg-white p-2 text-sm rounded-lg ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950">
                Sign in
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Sign in</DialogTitle>
                </DialogHeader>
                <DialogDescription>
                  Log in by writing your username and password
                </DialogDescription>
                <SigninForm />
              </DialogContent>
            </Dialog>
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
          </>
        )}
      </div>
    </nav>
  );
}
