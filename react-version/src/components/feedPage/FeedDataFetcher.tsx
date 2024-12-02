import { useEffect, useState } from "react";
import { getPolls } from "../../lib/API";
import FeedUI from "./feedUI";
import { Poll } from "../../lib/types";

export default function FeedDataFetcher() {
  const [polls, setPolls] = useState<Poll[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchPolls = async () => {
      try {
        let fetchedPolls: Poll[] = await getPolls();
        fetchedPolls = fetchedPolls.filter(
          (poll) =>
            poll.validUntil >
            new Date(Date.now()).toISOString().split(".")[0] + "Z"
        );
        setPolls(fetchedPolls);
      } catch (err) {
        console.error("Failed to fetch polls:", err);
        setError("Failed to fetch polls.");
      } finally {
        setLoading(false);
      }
    };

    fetchPolls();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (!polls) return <div>No polls available.</div>;

  return <FeedUI polls={polls} />;
}
