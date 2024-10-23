// app/lib/session.ts

import "server-only";
import { SignJWT, jwtVerify } from "jose";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export type SessionPayload = {
  username: string;
  email: string;
};

export type CheckSessionOptions = {
  username?: string;
  email?: string;
};

const secretKey = process.env.SESSION_SECRET;
const encodedKey = new TextEncoder().encode(secretKey);

export async function encrypt(payload: SessionPayload) {
  return new SignJWT(payload)
    .setProtectedHeader({ alg: "HS256" })
    .setIssuedAt()
    .setExpirationTime("7d") // Set expiry for 7 days
    .sign(encodedKey);
}

export async function decrypt(session: string | undefined = "") {
  try {
    const { payload } = await jwtVerify(session, encodedKey, {
      algorithms: ["HS256"],
    });
    return payload;
  } catch (error) {
    console.error("Failed to verify session", error);
    return null;
  }
}

export async function createSession(user: { username: string; email: string }) {
  const expiresAt = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000); // Expires in 7 days
  const session = await encrypt({ username: user.username, email: user.email });

  const cookieStore = cookies();
  cookieStore.set("session", session, {
    httpOnly: true,
    secure: true,
    expires: expiresAt,
    sameSite: "lax",
    path: "/",
  });
}

export async function updateSession() {
  const cookieStore = cookies();
  const session = cookieStore.get("session")?.value;
  const payload = await decrypt(session);

  if (!payload) return null;

  const expiresAt = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000);
  cookieStore.set("session", session ?? "", {
    httpOnly: true,
    secure: true,
    expires: expiresAt,
    sameSite: "lax",
    path: "/",
  });
}

export async function deleteSession() {
  const cookieStore = cookies();
  cookieStore.delete("session");
}

export async function logout() {
  await deleteSession();
  redirect("/");
}

export async function isUserSignedIn(options: CheckSessionOptions = {}) {
  // Retrieve the session cookie
  const session = cookies().get("session")?.value;

  // If the session cookie doesn't exist, the user is not signed in
  if (!session) {
    return false;
  }

  // Attempt to decrypt and verify the session
  const payload: any = await decrypt(session);

  // If decryption failed or the payload is invalid, return false
  if (!payload) {
    return false;
  }

  // Check for username and email in the decrypted payload if specified
  const { username, email } = options;

  if (username && payload.username !== username) {
    return false;
  }

  if (email && payload.email !== email) {
    return false;
  }

  // If all checks passed, the user is signed in
  return true;
}

export async function isSessionExpired() {
  // Retrieve the session cookie
  const session = cookies().get("session")?.value;

  // If the session cookie doesn't exist, return true (session is expired or invalid)
  if (!session) {
    return true;
  }

  // Attempt to decrypt and verify the session
  const payload: any = await decrypt(session);

  // If decryption failed or the payload is invalid, return true (session is expired or invalid)
  if (!payload) {
    return true;
  }

  // Check if the session has an expiration timestamp
  const { expiresAt } = payload;

  if (!expiresAt) {
    console.log("No expiration time found in session.");
    return true;
  }

  // Check if the current time is past the expiration time
  const currentTime = Date.now();
  const expirationTime = new Date(expiresAt).getTime();

  if (currentTime >= expirationTime) {
    // Session is expired
    return true;
  }

  // Session is still valid
  return false;
}

export async function getUserFromSession() {
  if ((await isSessionExpired()) || !(await isUserSignedIn())) return null;

  const session = cookies().get("session")?.value;

  const decryptedSess = await decrypt(session);

  if (!decryptedSess) return null;

  return decryptedSess;
}
