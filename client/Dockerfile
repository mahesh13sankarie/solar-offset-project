# Frontend Dockerfile
FROM node:20-alpine as build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . ./

# Expose port
EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host"]