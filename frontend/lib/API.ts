"use server";

import { redirect } from "next/navigation";
import {
  CheckSessionOptions,
  createSession,
  getUsernameFromSession,
  isSessionExpired,
  isUserSignedIn,
  logout,
} from "./session";
import { Poll, PollToSend, User } from "./types";

const url = "http://localhost:8080";

// TEMP

export async function getUser() {
  return await getUsernameFromSession();
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

// Function to get all polls
export async function getPolls() {
  const response = await fetch(`${url}/polls`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
    cache: "no-store", // Disable caching
  });

  if (!response.ok) {
    throw new Error("Failed to fetch users");
  }

  const polls = await response.json();

  return polls;
}

export async function getPollById(id: number) {
  const response = await fetch(`${url}/polls/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch user");
  }

  return await response.json();
}

// Function to create a user by making a POST request
export async function createPoll(poll: PollToSend) {
  const response = await fetch(`${url}/polls`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(poll), // Send the poll object as JSON in the body
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    console.error("Failed to create poll:", errorMessage);
    throw new Error("Failed to create user");
  }

  return await response.json();
}
