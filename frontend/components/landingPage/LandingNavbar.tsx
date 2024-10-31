import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import SigninForm from "./SigninForm";
import SignupForm from "./SignupForm";

export default function LandingNavbar() {
  return (
    <nav className="w-full h-18 p-4 flex items-center bg-neutral-100">
      <div>{/* LOGO */}</div>
      <div className="mr-0 ml-auto space-x-4">
        <Dialog>
          <DialogTrigger className="bg-white p-2 text-sm rounded-lg ml-auto mr-auto w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950">
            Sign in
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Sign in</DialogTitle>
            </DialogHeader>
            <DialogDescription>
              Log in by writing your email or username below (TEMP USE USERNAME)
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
      </div>
    </nav>
  );
}
