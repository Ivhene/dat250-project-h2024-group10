import { getPolls } from "@/lib/API";
import FeedUI from "./feedUI";
import { Poll } from "@/lib/types";

export default async function FeedDataFetcher() {
  let polls = await getPolls();

  if (polls) {
    polls = polls.filter(
      (poll: any) =>
        poll.validUntil > new Date(Date.now()).toISOString().split(".")[0] + "Z"
    );
  }

  return <FeedUI polls={polls} />;
}
