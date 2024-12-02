import CommonNavbar from "../commons/CommonNavbar";
import SigninForm from "../../components/landingPage/SigninForm";
import SignupForm from "../../components/landingPage/SignupForm";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../../components/ui/dialog";

export default function LandingPage() {
  const panelStyling =
    "w-72 bg-neutral-100 p-4 rounded-xl text-center h-52 border border-neutral-300 shadow-lg";

  return (
    <div className="h-full w-full">
      <div className="w-full flex flex-row gap-16 justify-center p-8 h-full items-center">
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
