export const generateId = (): number => {
  const timestampPart = Date.now(); // Current timestamp in milliseconds
  const randomPart = Math.floor(Math.random() * 1e6); // Random number between 0 and 999999
  return Number(`${timestampPart}${randomPart}`); // Combine both parts into a single number
};
