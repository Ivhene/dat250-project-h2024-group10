import { User } from "./types";

const url = "http://localhost:8080";

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

  return await response.json();
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

  return await response.json();
}
