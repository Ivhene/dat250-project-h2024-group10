"use client";

import { z } from "zod";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Poll } from "@/lib/types";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Button } from "@/components/ui/button";
import { createVote } from "@/lib/API";

type FeedUIProps = {
  polls: Poll[];
};

const FormSchema = z.object({
  voteOption: z.number().min(1, "Please select an option."),
});

export default function FeedUI({ polls }: FeedUIProps) {
  polls.sort((a, b) =>
    a.validUntil === b.validUntil ? 0 : a.publishedAt > b.publishedAt ? 1 : -1
  );

  return (
    <div className="h-full w-full p-8 grid grid-cols-3 gap-8 auto-rows-[minmax(200px,auto)]">
      {polls.map((poll) => {
        // Define a separate form instance for each poll
        const form = useForm<z.infer<typeof FormSchema>>({
          resolver: zodResolver(FormSchema),
        });

        function onSubmit(values: z.infer<typeof FormSchema>) {
          console.log(
            `Poll ID: ${poll.id} - Selected Option:`,
            values.voteOption
          );
          createVote(values.voteOption, poll);
        }

        return (
          <Card
            className="h-full bg-neutral-100 shadow-lg border-neutral-300 flex flex-col"
            key={poll.id}
          >
            <CardHeader>
              <CardTitle>{poll.question}</CardTitle>
              <CardDescription>Choose one of the options below</CardDescription>
            </CardHeader>
            <CardContent className="flex flex-grow flex-col justify-between">
              <Form {...form}>
                <form
                  onSubmit={form.handleSubmit(onSubmit)}
                  className="space-y-4 flex flex-col h-full"
                >
                  <FormField
                    control={form.control}
                    name="voteOption"
                    render={({ field }) => (
                      <FormItem className="flex-grow">
                        <FormControl>
                          <RadioGroup
                            onValueChange={field.onChange}
                            value={field.value}
                            className="flex flex-col space-y-2"
                          >
                            {poll.options
                              .sort(
                                (a, b) =>
                                  a.presentationOrder - b.presentationOrder
                              )
                              .map((option) => (
                                <FormItem
                                  className="flex items-center space-x-3"
                                  key={option.id}
                                >
                                  <FormControl>
                                    <RadioGroupItem value={option.id.toString()} />
                                  </FormControl>
                                  <FormLabel className="font-normal">
                                    {option.caption}
                                  </FormLabel>
                                </FormItem>
                              ))}
                          </RadioGroup>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <div className="mt-auto pt-4">
                    <Button
                      className="bg-white text-sm rounded-lg w-24 text-neutral-700 border-neutral-700 border hover:text-white hover:bg-neutral-950"
                      type="submit"
                    >
                      Submit
                    </Button>
                  </div>
                </form>
              </Form>
            </CardContent>
          </Card>
        );
      })}
    </div>
  );
}
