# clojure-ring-app

A minimal full-stack Clojure app — a LinkedIn-like directory where user profile
pages (`/in/:username`) are server-side rendered (SSR) for search engines, then
hydrated into interactive React on the client.

Stack:

- **Backend:** Ring + Jetty (server-rendered HTML via Hiccup)
- **Frontend:** ClojureScript + Helix (React), hydrates the SSR'd markup
- **Shared:** `.cljc` files (fake user data lives in
  `src/clojure_ring_app/data.cljc`)
- No database — static fake data for now.

## Structure

```
src/clojure_ring_app/
  data.cljc          shared fake data (server + client)
  core.clj           entry point, starts Jetty
  handler.clj        routing (/, /in/:username)
  views.clj          Hiccup SSR rendering
  client/core.cljs   hydration entry point
  client/ui.cljs     Helix components
resources/public/
  css/app.css
  js/                compiled by shadow-cljs (gitignored)
```

## Requirements

- [Clojure CLI](https://clojure.org/guides/install_clojure) (`brew install clojure`)
- Node + npm (for shadow-cljs and React)

## Build the client (ClojureScript)

```sh
clojure -M:cljs compile app
```

This downloads React into `node_modules` and compiles to `resources/public/js/main.js`.
For development with hot reload:

```sh
clojure -M:cljs watch app
```

## Run the server

```sh
clojure -M:dev
```

Then open:

- http://localhost:3399/           — home page (list of people)
- http://localhost:3399/in/ada-lovelace — SSR'd profile, hydrated on the client

## How SSR + hydration works

1. The server renders the profile to HTML (Hiccup) and embeds the user data as
   JSON in a `<script type="application/json" id="initial-data">` tag.
2. The client (`init` in `client/core.cljs`) reads that JSON and calls
   `hydrateRoot` to attach React to the already-rendered `#app` node.
3. The Helix components render the same markup as the Hiccup views, so React can
   reuse the server's DOM rather than replacing it.
