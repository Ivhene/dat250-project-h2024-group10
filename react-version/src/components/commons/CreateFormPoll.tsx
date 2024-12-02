"use client";

import { format } from "date-fns";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "../../components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../../components/ui/form";
import { Input } from "../../components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "../../components/ui/popover";
import { cn } from "../../lib/utils";
import { Calendar } from "../../components/ui/calendar";
import { CalendarIcon } from "lucide-react";
import { useState } from "react";
import {
  checkUserLoggedIn,
  createPoll,
  getUserByUsername,
  getUserFromToken,
} from "../../lib/API";
import { Poll, PollToSend } from "../../lib/types";

const formSchema = z.object({
  question: z.string().min(2).max(50),
  validUntil: z.date(),
});

type CreatePollFormProps = {
  close: () => void;
};

export default function CreatePollForm({ close }: CreatePollFormProps) {
  const [options, setOptions] = useState<string[]>(["", ""]); // Start with 2 empty options

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      question: "",
      validUntil: new Date(),
    },
  });

  // Add a new option
  const addOption = (): void => {
    setOptions([...options, ""]);
  };

  // Remove the last option
  const removeOption = (): void => {
    setOptions(options.slice(0, -1)); // Returns a new array without the last element
  };

  // Handle option text change
  const handleOptionChange = (index: number, value: string): void => {
    const newOptions = [...options];
    newOptions[index] = value;
    setOptions(newOptions);
  };

  async function onSubmit(values: z.infer<typeof formSchema>) {
    const token = localStorage.getItem("authToken") ?? "";
    const username = (await getUserFromToken(token)).username;
    // TEMP
    const user = await getUserByUsername(username, token ?? "");
    // Format date without milliseconds
    const publishedAt = new Date(Date.now()).toISOString().split(".")[0] + "Z";
    const validUntilFormatted =
      new Date(values.validUntil).toISOString().split(".")[0] + "Z";

    let newPoll: PollToSend = {
      // id: generateId(),
      question: values.question,
      publishedAt: publishedAt,
      validUntil: validUntilFormatted,
      createdUser: user,
      options: options
        .filter((option) => option.trim() !== "")
        .map((option, index) => {
          return {
            //id: generateId(),
            caption: option,
            count: 0,
            presentationOrder: index,
            votes: [],
          };
        }),
    };

    newPoll = await createPoll(newPoll, token ?? "");
    close();
    window.location.reload();
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-2">
        <FormField
          control={form.control}
          name="question"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Question</FormLabel>
              <FormControl>
                <Input placeholder="question" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="validUntil"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Valid Until</FormLabel>
              <Popover>
                <PopoverTrigger asChild className="w-full flex">
                  <FormControl>
                    <Button
                      variant="outline"
                      className={cn(
                        "text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value ? (
                        format(field.value, "PPP")
                      ) : (
                        <span>Pick a date</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={field.value}
                    onSelect={(date) => field.onChange(date)}
                    disabled={(date) => date < new Date()}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          )}
        />
        <h3>Options</h3>
        <div className="text-sm space-y-3">
          {options.map((option, index) => (
            <div
              key={index}
              className="option-container w-full flex items-center"
            >
              <label>{index + 1}:</label>
              <Input
                className="p-2 text-sm border ml-2 rounded-md border-neutral-400 flex-grow"
                type="text"
                value={option}
                onChange={(e) => handleOptionChange(index, e.target.value)}
                required
              />
            </div>
          ))}
        </div>
        <div className="grid grid-cols-2 gap-3 w-fit">
          <Button
            className="bg-neutral-100 w-full text-black border-neutral-200 border p-2 rounded-md mr-2"
            type="button"
            onClick={addOption}
          >
            Add Option
          </Button>
          <Button
            className="bg-neutral-100 w-full text-black border-neutral-200 border p-2 rounded-md mr-2"
            type="button"
            onClick={removeOption}
          >
            Remove Option
          </Button>
          <Button className="w-full" onClick={close}>
            Close
          </Button>
          <Button className="w-full" type="submit">
            Submit
          </Button>
        </div>
      </form>
    </Form>
  );
}
