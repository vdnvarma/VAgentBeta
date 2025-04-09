FROM mcr.microsoft.com/devcontainers/javascript-node:18

# Install additional dependencies
RUN apt-get update && apt-get install -y \
    git \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*