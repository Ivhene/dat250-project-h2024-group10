import { getPolls } from "@/lib/API";
import FeedUI from "./feedUI";

export default async function FeedDataFetcher() {
  const polls = await getPolls();

  return <FeedUI polls={polls} />;
}
