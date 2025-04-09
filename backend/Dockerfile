# Use an official Node.js image as the base
FROM node:16

# Install required tools
RUN apt-get update && apt-get install -y \
    openjdk-11-jdk \
    gcc \
    g++ \
    python3 \
    python3-pip

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port your app runs on
EXPOSE 8080

# Start the application
CMD ["npm", "start"]