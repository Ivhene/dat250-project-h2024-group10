import { Poll } from "@/lib/types";

type FeedUIProps = {
  polls: Poll[];
};

export default function FeedUI({ polls }: FeedUIProps) {
  console.log(polls);

  return <div className="h-full w-full bg-red-300 p-4">FEED UI HERE</div>;
}
