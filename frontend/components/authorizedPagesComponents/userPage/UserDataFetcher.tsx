"use client";

import { useEffect, useState } from "react";
import { getUserByUsername, getUserFromToken } from "@/lib/API";
import UserPageUI from "./UserPageUI";

export default function UserDataFetcher() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("authToken");
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        // Get user from token
        const userFromToken = await getUserFromToken(token);
        if (!userFromToken) {
          console.error("Invalid token");
          setLoading(false);
          return;
        }

        // Get full user data
        const username = userFromToken.username;
        const userDetails = await getUserByUsername(username, token);
        setUser(userDetails);
      } catch (error) {
        console.error("Error fetching user:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []); // Run once on component mount

  if (loading) return <div>Loading...</div>;
  if (!user) return <div>Error: User not found</div>;

  return <UserPageUI user={user} />;
}
