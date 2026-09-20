# TODO

Ideas and follow-ups for the clojure-ring-app skeleton.

## Development ergonomics

- [ ] **Backend hot reload in `dev`** — wrap the handler with
      `ring.middleware.reload/wrap-reload` (and `wrap-stacktrace`) so `.clj`
      changes are picked up per-request instead of restarting `make dev`.
- [ ] **Client live reload (HMR)** — `shadow-cljs watch` already recompiles, but
      the browser needs a manual refresh. Wire up shadow-cljs hot-reload so edits
      apply without refreshing.
- [ ] **Verify hydration in a real browser** — add a Playwright/Puppeteer smoke
      test to confirm no React hydration-mismatch warnings between the Hiccup SSR
      output and the Helix client components.

## Architecture

- [ ] **Replace hand-rolled routing with reitit** — `handler.clj` uses a regex
      dispatch. Reitit (data-driven) scales better as routes grow (and works in
      CLJS for client-side routing later).
- [ ] **Single-source rendering** — markup currently lives in two places (Hiccup
      server-side, Helix client-side) that must be kept in sync. Either accept the
      two renderers (documented), or explore rendering Helix `render-to-string` on
      the JVM (via a JS engine) for one source of truth.
- [ ] **Use shared `data.cljc` on the client too** — the client currently only
      gets data via the embedded JSON. Use `clojure-ring-app.data` directly in
      CLJS for client-side search/filtering later.

## Data & features

- [ ] **Real database** — replace the static `data.cljc` fake data with a real
      store (XTDB / Datomic / Postgres via `next.jdbc`) and a real `find-user`
      query. Add create/edit for profiles.
- [ ] **Client state management** — add `re-frame` once interactivity (editing,
      follow buttons, etc.) outgrows local component state.

## Correctness

- [ ] **Escape `</script>` in embedded JSON** — `views.clj` injects the user JSON
      into a `<script type="application/json">` tag raw. If user data ever contains
      `</script>`, it escapes the tag. Encode it (base64 or escape `</`).
- [ ] **Environment-based port** — `core.clj` hardcodes `:3399`. Read `PORT`
      (default 3399) so it matches the `PORT` Makefile var.

## Tooling / build

- [ ] **Upgrade to shadow-cljs 3.x** — pinned to `2.28.23` because shadow-cljs 3.x
      bundles a Closure Compiler that needs Java 21 (we're on 17). Move to Java 21
      (sdkman) + shadow-cljs `3.5.x`.
- [ ] **Slim the Docker runtime image** — currently the image ships the full JDK +
      Clojure CLI. Add a multi-stage build that produces an uberjar (via
      `tools.build`) and runs it on a JRE-only base.
- [ ] **Tests + CI** — no tests yet. Add `clojure.test`/`kaocha` for the handler
      and data, and a GitHub Actions workflow that builds the image and runs a
      smoke test.
