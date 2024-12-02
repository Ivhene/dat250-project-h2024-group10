import { getPolls } from "@/lib/API";
import FeedUI from "./feedUI";
import { Poll } from "@/lib/types";

export default async function FeedDataFetcher() {
  let polls: Poll[] = await getPolls();

  if (polls) {
    polls = polls.filter(
      (poll) =>
        poll.validUntil > new Date(Date.now()).toISOString().split(".")[0] + "Z"
    );
  }

  return <FeedUI polls={polls} />;
}
