import { getUserByUsername, getUserFromToken } from "@/lib/API";
import UserPageUI from "./UserPageUI";

export default async function UserDataFetcher() {
  const token = localStorage.getItem("authToken") ?? "";
  const username = (await getUserFromToken(token)).username;
  const user = await getUserByUsername(username, token); // hardcoded for now

  return <UserPageUI user={user} />;
}
