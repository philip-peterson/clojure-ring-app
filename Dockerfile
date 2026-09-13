FROM clojure:temurin-17-tools-deps

WORKDIR /app

COPY deps.edn .
RUN clojure -P

COPY src ./src

EXPOSE 3399

ENTRYPOINT ["clojure", "-M", "-m", "clojure-ring-app.core"]
