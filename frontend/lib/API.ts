"use server";

import { redirect } from "next/navigation";
import {
  CheckSessionOptions,
  createSession,
  getUserFromSession,
  isSessionExpired,
  isUserSignedIn,
  logout,
} from "./session";
import { User } from "./types";
import { NextResponse } from "next/server";

const url = "http://localhost:8080";

export async function getUser() {
  return await getUserFromSession();
}

export async function checkUserLoggedIn(user?: CheckSessionOptions) {
  return await isUserSignedIn(user);
}

export async function checkSessionExpired() {
  return await isSessionExpired();
}

export async function signOut() {
  logout();
}

export async function getUsers() {
  const response = await fetch(`${url}/users`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch users");
  }

  return await response.json();
}

export async function getUserByUsername(username: string) {
  const response = await fetch(`${url}/users/${username}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch users");
  }

  const user = await response.json();

  await createSession({ username: user.username, email: user.email });

  return user;
}

export async function createUser(user: User) {
  const response = await fetch(`${url}/users`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user), // Send the user object as JSON in the body
  });

  if (!response.ok) {
    throw new Error("Failed to create user");
  }

  const returnedUser = await response.json();

  await createSession({
    username: returnedUser.username,
    email: returnedUser.email,
  });

  return returnedUser;
}
