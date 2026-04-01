FROM mcr.microsoft.com/devcontainers/java:21

RUN mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://dl.yarnpkg.com/debian/pubkey.gpg \
      | gpg --dearmor \
      | tee /etc/apt/keyrings/yarn.gpg > /dev/null \
    && echo "deb [signed-by=/etc/apt/keyrings/yarn.gpg] https://dl.yarnpkg.com/debian stable main" \
      | tee /etc/apt/sources.list.d/yarn.list \
    && apt update \
    && apt-get update \
    && apt-get install -y \
        maven \
        curl \
        wget \
        gnupg \
        ca-certificates \
        unzip \
        fonts-liberation \
        libappindicator3-1 \
        libasound2 \
        libatk-bridge2.0-0 \
        libatk1.0-0 \
        libcups2 \
        libdbus-1-3 \
        libgdk-pixbuf2.0-0 \
        libnspr4 \
        libnss3 \
        libx11-xcb1 \
        libxcomposite1 \
        libxdamage1 \
        libxrandr2 \
        xdg-utils \
    --no-install-recommends \
\
    # Install Node.js 20
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
\
    # Install Angular CLI
    && npm install -g @angular/cli \
\
    # Add Google Chrome repo
    && mkdir -p /usr/share/keyrings \
    && curl -fsSL https://dl.google.com/linux/linux_signing_key.pub \
        | gpg --dearmor -o /usr/share/keyrings/google-linux-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-linux-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" \
        > /etc/apt/sources.list.d/google-chrome.list \
\
    # Install Chrome
    && apt-get update \
    && apt-get install -y google-chrome-stable \
\
    # Cleanup
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENV CHROME_BIN=/usr/bin/google-chrome-stable

WORKDIR /workspace

CMD ["sleep", "infinity"]