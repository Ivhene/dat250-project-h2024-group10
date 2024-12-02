# Stage 1: Build
FROM node:20 AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application for production
RUN npm run build

# Stage 2: Run
FROM node:20

# Set the working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/.next ./.next
COPY --from=build /app/package*.json ./

# Install only production dependencies
RUN npm install --production

# Expose the port that Next.js uses
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
