import { NextRequest, NextResponse } from "next/server";
import { isUserSignedIn, isSessionExpired } from "./lib/session";

const PUBLIC_PATHS = [
  "/",
  "/_next/static",
  "/_next/image",
  "/favicon.ico", // Add any other public paths as needed
];

export async function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;

  console.log("Path: " + pathname);

  // Allow access to specified public paths without authentication
  if (
    PUBLIC_PATHS.some((path) =>
      path === "/" ? pathname === "/" : pathname.startsWith(path)
    )
  ) {
    return NextResponse.next();
  }

  // Check if the user is signed in
  const signedIn = await isUserSignedIn();
  //  const expired = await isSessionExpired();

  console.log(
    `User signed in: ${signedIn}, Session expired: ${/*expired*/ ""}`
  );

  // If no session exists or session has expired, redirect to root ("/")
  if (!signedIn /*|| expired */) {
    console.log("Redirecting to root due to authentication failure.");
    return NextResponse.redirect(new URL("/", req.url));
  }

  // Allow the request to continue if the user is signed in
  return NextResponse.next();
}
