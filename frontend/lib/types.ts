export type User = {
  username: string;
  email: string;
  password: string;
  votes: Vote[];
  polls: Poll[];
};

export type Vote = {
  id: number;
  publishedAt: string;
  user: User;
  voteOption: VoteOption;
};

export type Poll = {
  id: number;
  question: string;
  publishedAt: string;
  validUntil: string;
  createdUser: User;
  options: VoteOption[];
};

export type VoteOption = {
  id: number | string;
  caption: string;
  presentationOrder: number;
  poll: Poll | null;
  votes: Vote[];
};

export type PollToSend = {
  id: number;
  question: string;
  publishedAt: string;
  validUntil: string;
  createdUser: User;
  options: VoteOptionToSend[];
};

export type VoteOptionToSend = {
  id: number;
  caption: string;
  presentationOrder: number;
  votes: Vote[];
};
