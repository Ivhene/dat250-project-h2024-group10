"use server";

import { Poll, PollToSend, User } from "./types";
import { revalidatePath } from "next/cache";

// const url = "http://localhost:8080"; // Base backend URL
const url = "https://dat250-project-h2024-group10.onrender.com";

/**
 * Login the user by fetching the token from the backend and storing it in localStorage.
 */
export async function login(username: string, password: string) {
  const response = await fetch(`${url}/api/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    console.error("Failed to log in:", errorMessage);
    throw new Error("Invalid username or password");
  }

  const { token } = await response.json();

  return token;
}

/**
 * Decode a JWT token to extract user details.
 */
export async function getUserFromToken(token: string) {
  try {
    const decoded: { sub: string; email?: string; exp?: number } =
      await decodeToken(token);

    // Optionally check expiration
    if (decoded.exp && decoded.exp * 1000 < Date.now()) {
      throw new Error("Token is expired");
    }

    return {
      username: decoded.sub,
    };
  } catch (error) {
    console.error("Failed to decode token:", error);
    throw new Error("Invalid token");
  }
}

/**
 * Wrapper function for authenticated API requests.
 */
export async function fetchWithAuth(
  endpoint: string,
  options: RequestInit = {},
  token: string
) {
  if (!token) {
    throw new Error("User is not authenticated");
  }

  const headers = {
    ...options.headers,
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  const response = await fetch(`${url}${endpoint}`, { ...options, headers });

  if (response.status === 401) {
    // Token expired or invalid, log the user out
    logout();
    throw new Error("Session expired. Please log in again.");
  }

  return response;
}

/**
 * Check if the user is currently logged in by verifying the token.
 */
export async function checkUserLoggedIn(token: string) {
  if (!token) {
    return false;
  }

  try {
    const user = await getUserFromToken(token); // Decode the token
    return !!user.username; // Check if the username exists
  } catch {
    logout();
    return false;
  }
}

/**
 * Logout the user by clearing the token.
 */
export async function logout() {
  localStorage.removeItem("authToken");
  window.location.href = "/login";
}

/**
 * Fetch all users (requires authentication).
 */
export async function getUsers() {
  const response = await fetch(`${url}/users`, { method: "GET" });

  if (!response.ok) {
    throw new Error("Failed to fetch users");
  }

  return await response.json();
}

/**
 * Fetch a user by username (requires authentication).
 */
export async function getUserByUsername(username: string, token: string) {
  const response = await fetchWithAuth(
    `/users/${username}`,
    {
      method: "GET",
      next: { revalidate: 300 },
    },
    token
  );

  if (!response.ok) {
    throw new Error("Failed to fetch user");
  }

  return await response.json();
}

/**
 * Create a new user.
 */
export async function createUser(user: User) {
  console.log("HI");
  const response = await fetch(`${url}/users`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  });

  if (!response.ok) {
    throw new Error("Failed to create user");
  }

  const { token } = await response.json();

  return token;
}

/**
 * Fetch all polls (requires authentication).
 */
export async function getPolls() {
  const response = await fetch(`${url}/polls`, {
    method: "GET",
    next: { revalidate: 300 },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch polls");
  }

  return await response.json();
}

/**
 * Fetch a poll by its ID (requires authentication).
 */
export async function getPollById(id: number) {
  const response = await fetch(`${url}/polls/${id}`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Failed to fetch poll");
  }

  return await response.json();
}

/**
 * Create a new poll (requires authentication).
 */
export async function createPoll(poll: PollToSend, token: string) {
  const response = await fetchWithAuth(
    "/polls",
    {
      method: "POST",
      body: JSON.stringify(poll),
    },
    token
  );
  if (!response.ok) {
    const errorMessage = await response.text();
    console.error("Failed to create poll:", errorMessage);
    throw new Error("Failed to create poll");
  }

  revalidatePath("/feed");
  revalidatePath("/profile");

  return await response.json();
}

/**
 * Create a vote for a poll option (requires authentication).
 */
export async function createVote(voteoptionId: number, poll: Poll) {
  const option = poll.options.find((option) => option.id === voteoptionId);

  if (!option) {
    return;
  }

  const vote = {
    // id: generateId(),
    publishedAt: new Date(Date.now()).toISOString().split(".")[0] + "Z",
    option: option,
  };

  const response = await fetch(
    `${url}/polls/${poll.id}/voteoptions/${voteoptionId}/votes`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // Set Content-Type to application/json
      },
      body: JSON.stringify(vote),
    }
  );

  if (!response.ok) {
    const errorMessage = await response.text();
    console.error("Failed to create vote:", errorMessage);
    throw new Error("Failed to create vote");
  }

  revalidatePath("/feed");
  revalidatePath("/profile");

  return await response.json();
}

export async function decodeToken(token: string) {
  try {
    const base64Url = token.split(".")[1]; // Extract the payload part
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/"); // Adjust Base64 format
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => `%${("00" + c.charCodeAt(0).toString(16)).slice(-2)}`)
        .join("")
    );

    return JSON.parse(jsonPayload); // Parse the JSON payload
  } catch (error) {
    console.error("Failed to decode token:", error);
    throw new Error("Invalid token");
  }
}
