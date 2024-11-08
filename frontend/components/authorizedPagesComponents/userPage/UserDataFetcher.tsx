import { getUserByUsername } from "@/lib/API";
import UserPageUI from "./UserPageUI";

export default async function UserDataFetcher() {
  const user = await getUserByUsername("Test"); // hardcoded for now

  return <UserPageUI user={user} />;
}
