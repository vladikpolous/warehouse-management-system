FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy only package.json and package-lock.json first (this helps with caching)
COPY package*.json ./

# Install dependencies (this will be cached if package*.json doesn't change)
RUN npm ci

# Copy the rest of the project files
COPY . .

# Run the Angular application
CMD ["npx", "ng", "serve", "--host", "0.0.0.0"]
