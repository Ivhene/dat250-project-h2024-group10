export type User = {
  username: string;
  email: string;
  password: string;
  salt: string;
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
  count: number;
};

export type PollToSend = {
  // id: number;
  question: string;
  publishedAt: string;
  validUntil: string;
  createdUser: User;
  options: VoteOptionToSend[];
};

export type VoteOptionToSend = {
  // id: number;
  caption: string;
  presentationOrder: number;
  count: number;
  votes: Vote[];
};

export type NavigationProp = {
  navigate: (page: string) => void;
};
