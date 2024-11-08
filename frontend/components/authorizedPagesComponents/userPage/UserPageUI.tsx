"use client";

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { User } from "@/lib/types";

type UserPageUIProps = {
  user: User;
};

export default function UserPageUI({ user }: UserPageUIProps) {
  console.log(user);

  return (
    <div className="w-full h-full p-4">
      <Accordion type="multiple">
        <AccordionItem value="polls">
          <AccordionTrigger>Your polls</AccordionTrigger>
          <AccordionContent className="grid grid-cols-3 gap-4">
            {user.polls.map((poll) => {
              const totalVotes = poll.options.reduce(
                (sum, option) => sum + option.votes.length,
                0
              );
              return (
                <Card
                  className="h-full bg-neutral-100 shadow-lg border-neutral-300 flex flex-col"
                  key={poll.id}
                >
                  <CardHeader>
                    <CardTitle>{poll.question}</CardTitle>
                    <CardDescription>Options</CardDescription>
                  </CardHeader>
                  <CardContent className="flex flex-grow flex-col justify-between">
                    <div className="flex flex-col space-y-2">
                      {poll.options
                        .sort(
                          (a, b) => a.presentationOrder - b.presentationOrder
                        )
                        .map((option) => (
                          <div
                            className="flex items-center justify-between border-b border-neutral-300"
                            key={option.id}
                          >
                            <p className="font-normal">{option.caption}</p>
                            <p className="">{`${(
                              (option.votes.length / totalVotes) *
                              100
                            ).toFixed(0)}%`}</p>
                          </div>
                        ))}
                    </div>
                  </CardContent>
                </Card>
              );
            })}
          </AccordionContent>
        </AccordionItem>
        <AccordionItem value="votes">
          <AccordionTrigger>Your votes</AccordionTrigger>
          <AccordionContent className="grid grid-cols-3 gap-4">
            {user.votes.map((vote) => {
              /*const totalVotes =
                vote.voteOption.poll?.options.reduce(
                  (sum, option) => sum + option.votes.length,
                  0
                ) ?? 0; */
              return (
                <Card
                  className="h-full bg-neutral-100 shadow-lg border-neutral-300 flex flex-col"
                  key={vote.id}
                >
                  <CardHeader>
                    <CardTitle>{"Vote"}</CardTitle>
                    <CardDescription>Options</CardDescription>
                  </CardHeader>
                  <CardContent className="flex flex-grow flex-col justify-between">
                    <div className="flex flex-col space-y-2">
                      {/*vote.voteOption.
                        .sort(
                          (a, b) => a.presentationOrder - b.presentationOrder
                        )
                        .map((option) => (
                          <div
                            className="flex items-center justify-between border-b border-neutral-300"
                            key={option.id}
                          >
                            <p className="font-normal">{option.caption}</p>
                            <p className="">{`${option.votes.length.toFixed(
                              0
                            )}%`}</p>
                          </div>
                        )) */}
                    </div>
                  </CardContent>
                </Card>
              );
            })}
          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  );
}
