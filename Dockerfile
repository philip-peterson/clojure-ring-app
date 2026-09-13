FROM clojure:temurin-17-tools-deps

# Node.js + npm (needed by shadow-cljs and the React npm deps)
RUN ARCH="$(uname -m)"; \
    case "$ARCH" in \
      aarch64) NODE_ARCH=arm64 ;; \
      x86_64)  NODE_ARCH=x64 ;; \
      *) echo "Unsupported architecture: $ARCH"; exit 1 ;; \
    esac; \
    apt-get update \
    && apt-get install -y --no-install-recommends curl xz-utils \
    && rm -rf /var/lib/apt/lists/* \
    && curl -fsSL "https://nodejs.org/dist/v20.18.1/node-v20.18.1-linux-${NODE_ARCH}.tar.xz" \
       | tar -xJ -C /usr/local --strip-components=1

WORKDIR /app

# Prefetch Clojure deps and install npm deps (cached until these files change)
COPY deps.edn shadow-cljs.edn package.json package-lock.json ./
RUN clojure -P -M:cljs && npm install

COPY src ./src
COPY resources ./resources

# Compile the ClojureScript bundle into resources/public/js
RUN clojure -M:cljs compile app

EXPOSE 3399
CMD ["clojure", "-M", "-m", "clojure-ring-app.core"]
