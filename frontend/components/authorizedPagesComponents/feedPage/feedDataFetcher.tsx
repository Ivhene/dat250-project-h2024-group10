import { getPolls } from "@/lib/API";
import FeedUI from "./feedUI";
import { Poll } from "@/lib/types";

export default async function FeedDataFetcher() {
  let polls: Poll[] = [];
  try {
    polls = await getPolls();

    if (polls) {
      polls = polls.filter(
        (poll: any) =>
          poll.validUntil >
          new Date(Date.now()).toISOString().split(".")[0] + "Z"
      );
    }
  } catch {
    polls = [];
  }

  return <FeedUI polls={polls} />;
}
